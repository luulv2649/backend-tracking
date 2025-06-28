package com.luulv.vn.backendtracking.repository;

import com.luulv.vn.backendtracking.entity.Product;
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
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Tìm sản phẩm theo URL
    Optional<Product> findByUrl(String url);

    // Tìm sản phẩm theo type
    List<Product> findByType(String type);

    // Tìm sản phẩm theo notification status
    List<Product> findByIsNotify(Integer isNotify);

    // Tìm sản phẩm được tạo trong khoảng thời gian
    List<Product> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate);

    // Tìm sản phẩm theo type và notification status
    List<Product> findByTypeAndIsNotify(String type, Integer isNotify);

    // Tìm sản phẩm với URL chứa keyword
    @Query("SELECT p FROM Product p WHERE p.url LIKE %:keyword%")
    List<Product> findByUrlContaining(@Param("keyword") String keyword);

    // Đếm số lượng sản phẩm theo type
    @Query("SELECT COUNT(p) FROM Product p WHERE p.type = :type")
    Long countByType(@Param("type") String type);

    // Tìm sản phẩm với pagination và filter
    @Query("SELECT p FROM Product p WHERE " +
            "(:type IS NULL OR p.type = :type) AND " +
            "(:isNotify IS NULL OR p.isNotify = :isNotify)")
    Page<Product> findWithFilters(@Param("type") String type,
                                  @Param("isNotify") Integer isNotify,
                                  Pageable pageable);

    // Kiểm tra URL đã tồn tại chưa
    boolean existsByUrl(String url);

    // Lấy danh sách các type duy nhất
    @Query("SELECT DISTINCT p.type FROM Product p WHERE p.type IS NOT NULL ORDER BY p.type")
    List<String> findDistinctTypes();

    @Query(value = "SELECT * FROM product p WHERE " +
            "(:type IS NULL OR p.type = :type) AND " +
            "(:url IS NULL OR LOWER(CAST(p.url AS TEXT)) LIKE LOWER(CONCAT('%', :url, '%'))) AND " +
            "(:isNotify IS NULL OR p.is_notify = :isNotify)",
            countQuery = "SELECT COUNT(*) FROM product p WHERE " +
                    "(:type IS NULL OR p.type = :type) AND " +
                    "(:url IS NULL OR LOWER(CAST(p.url AS TEXT)) LIKE LOWER(CONCAT('%', :url, '%'))) AND " +
                    "(:isNotify IS NULL OR p.is_notify = :isNotify)",
            nativeQuery = true)
    Page<Product> search(
            @Param("type") String type,
            @Param("url") String url,
            @Param("isNotify") Integer isNotify,
            Pageable pageable
    );
}
