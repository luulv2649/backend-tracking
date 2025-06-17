package com.luulv.vn.backendtracking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url", nullable = false)
    @NotBlank(message = "URL không được để trống")
    @Size(max = 255, message = "URL không được vượt quá 255 ký tự")
    private String url;

    @Column(name = "type")
    @Size(max = 255, message = "Type không được vượt quá 255 ký tự")
    private String type;

    @Column(name = "is_notify", nullable = false)
    private Integer isNotify = 1;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDate updatedAt;

    // Constructor cho việc tạo mới product
    public Product(String url, String type) {
        this.url = url;
        this.type = type;
        this.isNotify = 1;
    }

    // Method để toggle notification
    public void toggleNotification() {
        this.isNotify = this.isNotify == 1 ? 0 : 1;
    }

    // Method kiểm tra notification có được bật không
    public boolean isNotificationEnabled() {
        return this.isNotify == 1;
    }
}
