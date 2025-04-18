package com.example.mapstructdemo.service;

import com.example.mapstructdemo.dto.ProductDto;
import com.example.mapstructdemo.entity.Product;
import com.example.mapstructdemo.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class MapStructDemoService implements CommandLineRunner {

    private final ProductMapper productMapper;

    @Autowired
    public MapStructDemoService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public void run(String... args) {
        System.out.println("========== MapStruct 測試 ==========");

        // 測試 Entity -> DTO
        System.out.println("\n------ Entity 到 DTO 的轉換 ------");
        Product product = createSampleProduct();
        ProductDto productDto = productMapper.productToProductDto(product);
        printProductDto(productDto);

        // 測試 DTO -> Entity
        System.out.println("\n------ DTO 到 Entity 的轉換 ------");
        ProductDto dto = createSampleDto();
        Product entity = productMapper.productDtoToProduct(dto);
        printProduct(entity);

        // 測試 Map -> DTO
        System.out.println("\n------ Map 到 DTO 的轉換 ------");
        Map<String, Object> productMap = createSampleMap();
        ProductDto mapDto = productMapper.mapToProductDto(productMap);
        printProductDto(mapDto);

        // 測試更新現有實體
        System.out.println("\n------ 使用 DTO 更新現有 Entity ------");
        Product existingProduct = createSampleProduct();
        System.out.println("更新前: " + existingProduct.getName() + ", 可用性: " + existingProduct.isAvailable());

        ProductDto updateDto = ProductDto.builder()
                .name("已更新的產品名稱")
                .inStock(true)
                .build();

        productMapper.updateProductFromDto(updateDto, existingProduct);
        System.out.println("更新後: " + existingProduct.getName() + ", 可用性: " + existingProduct.isAvailable());

        System.out.println("\n========== 測試完成 ==========");
    }

    private Product createSampleProduct() {
        return Product.builder()
                .id(1L)
                .name("測試產品")
                .description("這是一個測試產品")
                .price(new BigDecimal("199.99"))
                .sku("TEST-001")
                .available(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private ProductDto createSampleDto() {
        return ProductDto.builder()
                .id(2L)
                .name("測試DTO")
                .description("這是一個從DTO創建的產品")
                .price(new BigDecimal("299.99"))
                .sku("DTO-002")
                .inStock(true)
                .createdDate("2023-11-05 08:30:00")
                .build();
    }

    private Map<String, Object> createSampleMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("product_id", 3L);
        map.put("product_name", "從Map創建的產品");
        map.put("product_desc", "這是一個從Map創建的測試產品");
        map.put("product_price", new BigDecimal("399.99"));
        map.put("product_sku", "MAP-003");
        map.put("is_available", true);
        return map;
    }

    private void printProductDto(ProductDto dto) {
        System.out.println("ProductDto{" +
                "id=" + dto.getId() +
                ", name='" + dto.getName() + '\'' +
                ", description='" + dto.getDescription() + '\'' +
                ", price=" + dto.getPrice() +
                ", sku='" + dto.getSku() + '\'' +
                ", inStock=" + dto.isInStock() +
                ", createdDate='" + dto.getCreatedDate() + '\'' +
                '}');
    }

    private void printProduct(Product product) {
        System.out.println("Product{" +
                "id=" + product.getId() +
                ", name='" + product.getName() + '\'' +
                ", description='" + product.getDescription() + '\'' +
                ", price=" + product.getPrice() +
                ", sku='" + product.getSku() + '\'' +
                ", available=" + product.isAvailable() +
                ", createdAt=" + product.getCreatedAt() +
                ", updatedAt=" + product.getUpdatedAt() +
                '}');
    }
}