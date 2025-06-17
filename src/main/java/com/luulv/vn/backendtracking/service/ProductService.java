package com.luulv.vn.backendtracking.service;


import com.luulv.vn.backendtracking.dto.ProductRequestDto;
import com.luulv.vn.backendtracking.dto.ProductResponseDto;
import com.luulv.vn.backendtracking.entity.Product;
import com.luulv.vn.backendtracking.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Tạo sản phẩm mới
     */
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        log.info("Creating new product with URL: {}", requestDto.getUrl());

        // Kiểm tra URL đã tồn tại chưa
        if (productRepository.existsByUrl(requestDto.getUrl())) {
            throw new RuntimeException("URL đã tồn tại trong hệ thống");
        }

        Product product = new Product();
        product.setUrl(requestDto.getUrl());
        product.setType(requestDto.getType());
        product.setIsNotify(requestDto.getIsNotify() != null ? requestDto.getIsNotify() : 1);

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());

        return ProductResponseDto.fromEntity(savedProduct);
    }

    /**
     * Lấy tất cả sản phẩm
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        log.info("Fetching all products");

        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return products.stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Lấy sản phẩm theo ID
     */
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Integer id) {
        log.info("Fetching product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        return ProductResponseDto.fromEntity(product);
    }

    /**
     * Cập nhật sản phẩm
     */
    public ProductResponseDto updateProduct(Integer id, ProductRequestDto requestDto) {
        log.info("Updating product with ID: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        // Kiểm tra URL mới có trùng với sản phẩm khác không
        if (!existingProduct.getUrl().equals(requestDto.getUrl()) &&
                productRepository.existsByUrl(requestDto.getUrl())) {
            throw new RuntimeException("URL đã tồn tại trong hệ thống");
        }

        existingProduct.setUrl(requestDto.getUrl());
        existingProduct.setType(requestDto.getType());
        existingProduct.setIsNotify(requestDto.getIsNotify() != null ? requestDto.getIsNotify() : 1);

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());

        return ProductResponseDto.fromEntity(updatedProduct);
    }

    /**
     * Xóa sản phẩm
     */
    public void deleteProduct(Integer id) {
        log.info("Deleting product with ID: {}", id);

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy sản phẩm với ID: " + id);
        }

        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }

    /**
     * Lấy sản phẩm theo type
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByType(String type) {
        log.info("Fetching products with type: {}", type);

        List<Product> products = productRepository.findByType(type);
        return products.stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Toggle notification cho sản phẩm
     */
    public ProductResponseDto toggleNotification(Integer id) {
        log.info("Toggling notification for product ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        product.toggleNotification();
        Product updatedProduct = productRepository.save(product);

        log.info("Notification toggled for product ID: {}, new status: {}",
                id, updatedProduct.isNotificationEnabled());

        return ProductResponseDto.fromEntity(updatedProduct);
    }

    /**
     * Lấy sản phẩm với pagination và filter
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsWithPagination(
            String type, Integer isNotify, int page, int size, String sortBy, String sortDir) {

        log.info("Fetching products with pagination - page: {}, size: {}, type: {}, isNotify: {}",
                page, size, type, isNotify);

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productRepository.findWithFilters(type, isNotify, pageable);

        return productPage.map(ProductResponseDto::fromEntity);
    }

    /**
     * Lấy danh sách các type duy nhất
     */
    @Transactional(readOnly = true)
    public List<String> getDistinctTypes() {
        log.info("Fetching distinct product types");
        return productRepository.findDistinctTypes();
    }

    /**
     * Tìm kiếm sản phẩm theo URL
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProductsByUrl(String keyword) {
        log.info("Searching products by URL keyword: {}", keyword);

        List<Product> products = productRepository.findByUrlContaining(keyword);
        return products.stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thống kê sản phẩm
     */
    @Transactional(readOnly = true)
    public ProductStatistics getProductStatistics() {
        log.info("Fetching product statistics");

        long totalProducts = productRepository.count();
        long activeNotifications = productRepository.findByIsNotify(1).size();
        long inactiveNotifications = productRepository.findByIsNotify(0).size();

        return new ProductStatistics(totalProducts, activeNotifications, inactiveNotifications);
    }

    // Inner class cho statistics
    public static class ProductStatistics {
        private final long totalProducts;
        private final long activeNotifications;
        private final long inactiveNotifications;

        public ProductStatistics(long totalProducts, long activeNotifications, long inactiveNotifications) {
            this.totalProducts = totalProducts;
            this.activeNotifications = activeNotifications;
            this.inactiveNotifications = inactiveNotifications;
        }

        // Getters
        public long getTotalProducts() { return totalProducts; }
        public long getActiveNotifications() { return activeNotifications; }
        public long getInactiveNotifications() { return inactiveNotifications; }
    }
}
