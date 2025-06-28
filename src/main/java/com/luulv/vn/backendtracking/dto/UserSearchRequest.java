package com.luulv.vn.backendtracking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRequest {
    private String username;
    private String fullName;
    private LocalDate registerDateFrom;
    private LocalDate registerDateTo;
    private LocalDate expiredDateFrom;
    private LocalDate expiredDateTo;
    private Integer status;
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDirection = "ASC";
}

