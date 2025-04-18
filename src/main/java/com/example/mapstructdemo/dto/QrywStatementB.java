package com.example.mapstructdemo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class QrywStatementB {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private QryStatementTranrsAddress address;
    private QryStatementTranrsProduct product;
}
