package com.luulv.vn.backendtracking.controller;

import com.luulv.vn.backendtracking.dto.ApiResponse;
import com.luulv.vn.backendtracking.dto.UserRequestDTO;
import com.luulv.vn.backendtracking.dto.UserResponseDTO;
import com.luulv.vn.backendtracking.dto.UserSearchRequest;
import com.luulv.vn.backendtracking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Tạo user mới
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> create(
            @Valid @RequestBody UserRequestDTO requestDto) {

        try {
            UserResponseDTO userResponseDTO = userService.createUser(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo sản phẩm thành công", userResponseDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Tạo sản phẩm thất bại", e.getMessage()));
        }
    }

    // Cập nhật user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateProduct(
            @PathVariable Integer id, @Valid @RequestBody UserRequestDTO requestDto) {

        try {
            UserResponseDTO product = userService.updateUser(id, requestDto);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật sản phẩm thành công", product));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Cập nhật sản phẩm thất bại", e.getMessage()));
        }
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> searchUsersWithBody(
            @RequestBody UserSearchRequest request
    ) {
        Page<UserResponseDTO> response = userService.searchUsers(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // Cập nhật user
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> detail(@PathVariable Integer id) {
        try {
            UserResponseDTO product = userService.detail(id);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật sản phẩm thành công", product));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Cập nhật sản phẩm thất bại", e.getMessage()));
        }
    }
}
