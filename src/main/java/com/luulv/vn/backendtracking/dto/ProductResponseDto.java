package com.luulv.vn.backendtracking.dto;

import com.luulv.vn.backendtracking.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Integer id;
    private String url;
    private String type;
    private Integer isNotify;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    // Constructor từ Entity
    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.url = product.getUrl();
        this.type = product.getType();
        this.isNotify = product.getIsNotify();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }

    // Static factory method
    public static ProductResponseDto fromEntity(Product product) {
        return new ProductResponseDto(product);
    }
}
