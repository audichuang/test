package com.example.mapstructdemo.service;

import com.example.mapstructdemo.dto.ProductDto;
import com.example.mapstructdemo.entity.Product;
import com.example.mapstructdemo.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ProductDto convertEntityToDto() {
        // 創建一個示例 Product 實體
        Product product = Product.builder()
                .id(1L)
                .name("筆記型電腦")
                .description("高性能筆記本電腦，適合遊戲和專業工作")
                .price(new BigDecimal("1299.99"))
                .sku("LAPTOP-001")
                .available(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 使用 MapStruct 將 Entity 轉換為 DTO
        return productMapper.productToProductDto(product);
    }

    public Product convertDtoToEntity() {
        // 創建一個示例 ProductDto
        ProductDto productDto = ProductDto.builder()
                .id(2L)
                .name("智能手機")
                .description("最新智能手機，配備高清相機和長效電池")
                .price(new BigDecimal("799.99"))
                .sku("PHONE-002")
                .inStock(true)
                .createdDate("2023-11-01 10:15:00")
                .build();

        // 使用 MapStruct 將 DTO 轉換為 Entity
        return productMapper.productDtoToProduct(productDto);
    }

    public ProductDto convertMapToDto() {
        // 創建一個示例 Map
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("product_id", 3L);
        productMap.put("product_name", "平板電腦");
        productMap.put("product_desc", "輕薄便攜的平板電腦，適合閱讀和媒體消費");
        productMap.put("product_price", new BigDecimal("499.99"));
        productMap.put("product_sku", "TABLET-003");
        productMap.put("is_available", true);

        // 使用 MapStruct 將 Map 轉換為 DTO
        return productMapper.mapToProductDto(productMap);
    }

    public Product updateEntityFromDto() {
        // 創建一個已存在的 Product 實體
        Product existingProduct = Product.builder()
                .id(4L)
                .name("舊產品名稱")
                .description("舊產品描述")
                .price(new BigDecimal("99.99"))
                .sku("OLD-001")
                .available(false)
                .createdAt(LocalDateTime.now().minusDays(30))
                .updatedAt(LocalDateTime.now().minusDays(30))
                .build();

        // 創建一個包含更新信息的 DTO
        ProductDto updateDto = ProductDto.builder()
                .id(4L)
                .name("更新後的產品名稱")
                .description("更新後的產品描述")
                .price(new BigDecimal("129.99"))
                .inStock(true)
                .build();

        // 使用 MapStruct 更新實體
        productMapper.updateProductFromDto(updateDto, existingProduct);

        return existingProduct;
    }
}