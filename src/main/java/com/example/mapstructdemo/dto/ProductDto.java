package com.example.mapstructdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private boolean inStock; // 注意這裡的命名與實體類中不同
    private String createdDate; // 注意這裡的類型與實體類中不同
}