package com.luulv.vn.backendtracking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "URL không được để trống")
    private String url;

    @Size(max = 255, message = "Type không được vượt quá 255 ký tự")
    private String type;

    private Integer isNotify = 1;
}
