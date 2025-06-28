package com.luulv.vn.backendtracking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequestDto {

    private String url;

    private String type;

    private Integer isNotify = 1;

    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDirection = "ASC";
}
