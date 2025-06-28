package com.luulv.vn.backendtracking.repository;

import com.luulv.vn.backendtracking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%")
    List<User> searchByUsername(@Param("username") String username);

    List<User> findByStatus(Integer status);

    boolean existsByUsername(String username);

    @Query(value = """
    SELECT * FROM users u
    WHERE (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
      AND (:fullName IS NULL OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :fullName, '%')))
      AND (COALESCE(:registerDateFrom, NULL::date) IS NULL OR u.register_date >= :registerDateFrom)
      AND (COALESCE(:registerDateTo, NULL::date) IS NULL OR u.register_date <= :registerDateTo)
      AND (COALESCE(:expiredDateFrom, NULL::date) IS NULL OR u.expired_date >= :expiredDateFrom)
      AND (COALESCE(:expiredDateTo, NULL::date) IS NULL OR u.expired_date <= :expiredDateTo)
      AND (:status IS NULL OR u.status = :status)
    """, nativeQuery = true
    )
    Page<User> searchUsers(
            @Param("username") String username,
            @Param("fullName") String fullName,
            @Param("registerDateFrom") LocalDate registerDateFrom,
            @Param("registerDateTo") LocalDate registerDateTo,
            @Param("expiredDateFrom") LocalDate expiredDateFrom,
            @Param("expiredDateTo") LocalDate expiredDateTo,
            @Param("status") Integer status,
            Pageable pageable
    );
}
