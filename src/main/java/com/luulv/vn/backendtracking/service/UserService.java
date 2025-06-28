package com.luulv.vn.backendtracking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luulv.vn.backendtracking.dto.ProductResponseDto;
import com.luulv.vn.backendtracking.dto.UserRequestDTO;
import com.luulv.vn.backendtracking.dto.UserResponseDTO;
import com.luulv.vn.backendtracking.dto.UserSearchRequest;
import com.luulv.vn.backendtracking.entity.User;
import com.luulv.vn.backendtracking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new RuntimeException("Username đã tồn tại: " + userRequestDTO.getUsername());
        }

        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .fullName(userRequestDTO.getFullName())
                .registerDate(userRequestDTO.getRegisterDate())
                .expiredDate(userRequestDTO.getRegisterDate().plusMonths(1))
                .status(1)
                .build();
        User savedUser = userRepository.save(user);

        return mapper.convertValue(savedUser, UserResponseDTO.class);
    }

    public UserResponseDTO updateUser(Integer id, UserRequestDTO userUpdateDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + id));

        // Kiểm tra username trùng lặp nếu có thay đổi
        if (userUpdateDTO.getUsername() != null
                && !existingUser.getUsername().equals(userUpdateDTO.getUsername())
                && userRepository.existsByUsername(userUpdateDTO.getUsername())) {
            throw new RuntimeException("Username đã tồn tại: " + userUpdateDTO.getUsername());
        }

        existingUser.setRegisterDate(userUpdateDTO.getRegisterDate());
        existingUser.setExpiredDate(userUpdateDTO.getRegisterDate().plusMonths(1));
        existingUser.setStatus(userUpdateDTO.getStatus());
        User updatedUser = userRepository.save(existingUser);
        return mapper.convertValue(updatedUser, UserResponseDTO.class);
    }

    public Page<UserResponseDTO> searchUsers(UserSearchRequest request) {
        // Tạo Pageable object
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        // Thực hiện tìm kiếm
        Page<User> userPage = userRepository.searchUsers(
                request.getUsername(),
                request.getFullName(),
                request.getRegisterDateFrom(),
                request.getRegisterDateTo(),
                request.getExpiredDateFrom(),
                request.getExpiredDateTo(),
                request.getStatus(),
                pageable
        );

        return userPage.map(x -> mapper.convertValue(x, UserResponseDTO.class));
    }

    public UserResponseDTO detail(Integer id) {
        return mapper.convertValue(userRepository.findById(id).orElseThrow(() -> new RuntimeException("Lỗi")), UserResponseDTO.class);
    }
}
