package com.luulv.vn.backendtracking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String fullName;
    private LocalDate registerDate;
    private LocalDate expiredDate;
    private Integer status;
    private String statusDescription;
    private boolean isExpired;

    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
        this.isExpired = expiredDate.isBefore(LocalDate.now());
    }
}
