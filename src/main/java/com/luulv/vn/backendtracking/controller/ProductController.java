package com.luulv.vn.backendtracking.controller;


import com.luulv.vn.backendtracking.dto.ApiResponse;
import com.luulv.vn.backendtracking.dto.ProductRequestDto;
import com.luulv.vn.backendtracking.dto.ProductResponseDto;
import com.luulv.vn.backendtracking.dto.ProductSearchRequestDto;
import com.luulv.vn.backendtracking.dto.UserResponseDTO;
import com.luulv.vn.backendtracking.dto.UserSearchRequest;
import com.luulv.vn.backendtracking.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * Tạo sản phẩm mới
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @RequestBody ProductRequestDto requestDto) {

        log.info("Received request to create product: {}", requestDto);

        try {
            ProductResponseDto product = productService.createProduct(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo sản phẩm thành công", product));
        } catch (Exception e) {
            log.error("Error creating product: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Tạo sản phẩm thất bại", e.getMessage()));
        }
    }

    /**
     * Lấy tất cả sản phẩm
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts() {
        log.info("Received request to get all products");

        try {
            List<ProductResponseDto> products = productService.getAllProducts();
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Error fetching products: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi khi lấy danh sách sản phẩm", e.getMessage()));
        }
    }

    /**
     * Lấy sản phẩm theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable Integer id) {
        log.info("Received request to get product with ID: {}", id);

        try {
            ProductResponseDto product = productService.getProductById(id);
            return ResponseEntity.ok(ApiResponse.success(product));
        } catch (Exception e) {
            log.error("Error fetching product with ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy sản phẩm", e.getMessage()));
        }
    }

    /**
     * Cập nhật sản phẩm
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @PathVariable Integer id, @Valid @RequestBody ProductRequestDto requestDto) {

        log.info("Received request to update product with ID: {}", id);

        try {
            ProductResponseDto product = productService.updateProduct(id, requestDto);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật sản phẩm thành công", product));
        } catch (Exception e) {
            log.error("Error updating product with ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Cập nhật sản phẩm thất bại", e.getMessage()));
        }
    }

    /**
     * Xóa sản phẩm
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Integer id) {
        log.info("Received request to delete product with ID: {}", id);

        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa sản phẩm thành công"));
        } catch (Exception e) {
            log.error("Error deleting product with ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Xóa sản phẩm thất bại", e.getMessage()));
        }
    }

    /**
     * Lấy sản phẩm theo type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProductsByType(@PathVariable String type) {
        log.info("Received request to get products with type: {}", type);

        try {
            List<ProductResponseDto> products = productService.getProductsByType(type);
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Error fetching products by type {}: ", type, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi khi lấy sản phẩm theo type", e.getMessage()));
        }
    }

    /**
     * Toggle notification cho sản phẩm
     */
    @PatchMapping("/{id}/toggle-notification")
    public ResponseEntity<ApiResponse<ProductResponseDto>> toggleNotification(@PathVariable Integer id) {
        log.info("Received request to toggle notification for product ID: {}", id);

        try {
            ProductResponseDto product = productService.toggleNotification(id);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thông báo thành công", product));
        } catch (Exception e) {
            log.error("Error toggling notification for product ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Cập nhật thông báo thất bại", e.getMessage()));
        }
    }

    /**
     * Lấy sản phẩm với pagination
     */
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getProductsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer isNotify) {

        log.info("Received request for paginated products - page: {}, size: {}", page, size);

        try {
            Page<ProductResponseDto> products = productService.getProductsWithPagination(
                    type, isNotify, page, size, sortBy, sortDir);
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Error fetching paginated products: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi khi lấy danh sách sản phẩm", e.getMessage()));
        }
    }

    /**
     * Lấy danh sách các type duy nhất
     */
    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<String>>> getDistinctTypes() {
        log.info("Received request to get distinct product types");

        try {
            List<String> types = productService.getDistinctTypes();
            return ResponseEntity.ok(ApiResponse.success(types));
        } catch (Exception e) {
            log.error("Error fetching distinct types: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi khi lấy danh sách type", e.getMessage()));
        }
    }

    /**
     * Lấy thống kê sản phẩm
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<ProductService.ProductStatistics>> getStatistics() {
        log.info("Received request to get product statistics");

        try {
            ProductService.ProductStatistics stats = productService.getProductStatistics();
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            log.error("Error fetching statistics: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi khi lấy thống kê", e.getMessage()));
        }
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> search(
            @RequestBody ProductSearchRequestDto request
    ) {
        Page<ProductResponseDto> response = productService.search(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
