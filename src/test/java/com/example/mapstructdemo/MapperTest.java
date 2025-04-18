package com.example.mapstructdemo;

import com.example.mapstructdemo.dto.ProductDto;
import com.example.mapstructdemo.entity.Product;
import com.example.mapstructdemo.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testProductToProductDto() {
        // 創建測試數據
        Product product = Product.builder()
                .id(1L)
                .name("測試產品")
                .description("產品描述")
                .price(new BigDecimal("99.99"))
                .sku("TEST-123")
                .available(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 執行轉換
        ProductDto dto = productMapper.productToProductDto(product);

        // 驗證結果
        assertNotNull(dto);
        assertEquals(product.getId(), dto.getId());
        assertEquals(product.getName(), dto.getName());
        assertEquals(product.getDescription(), dto.getDescription());
        assertEquals(product.getPrice(), dto.getPrice());
        assertEquals(product.getSku(), dto.getSku());
        assertEquals(product.isAvailable(), dto.isInStock());
        assertNotNull(dto.getCreatedDate());
    }

    @Test
    public void testProductDtoToProduct() {
        // 創建測試數據
        ProductDto dto = ProductDto.builder()
                .id(2L)
                .name("DTO測試")
                .description("DTO描述")
                .price(new BigDecimal("199.99"))
                .sku("DTO-456")
                .inStock(false)
                .createdDate("2023-01-01 12:00:00")
                .build();

        // 執行轉換
        Product product = productMapper.productDtoToProduct(dto);

        // 驗證結果
        assertNotNull(product);
        assertEquals(dto.getId(), product.getId());
        assertEquals(dto.getName(), product.getName());
        assertEquals(dto.getDescription(), product.getDescription());
        assertEquals(dto.getPrice(), product.getPrice());
        assertEquals(dto.getSku(), product.getSku());
        assertEquals(dto.isInStock(), product.isAvailable());
        assertNotNull(product.getUpdatedAt());
    }
}