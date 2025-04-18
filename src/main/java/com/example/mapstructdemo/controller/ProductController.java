package com.example.mapstructdemo.controller;

import com.example.mapstructdemo.dto.ProductDto;
import com.example.mapstructdemo.entity.Product;
import com.example.mapstructdemo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 產品控制器
 * 提供產品數據轉換相關的REST API端點
 * 展示了MapStruct的各種基本使用場景：
 * 1. Entity 到 DTO的轉換
 * 2. DTO 到 Entity的轉換
 * 3. Map 到 DTO的轉換
 * 4. 使用DTO更新現有Entity
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "產品映射", description = "展示使用MapStruct進行各種基本數據映射操作的API")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 實體到DTO轉換示例
     * 演示如何將Product實體轉換為ProductDto
     * 
     * 預期返回：
     * {
     * "id": 1,
     * "name": "筆記型電腦",
     * "description": "高性能筆記本電腦，適合遊戲和專業工作",
     * "price": 1299.99,
     * "sku": "LAPTOP-001",
     * "inStock": true,
     * "createdDate": "2023-11-20 15:30:45"
     * }
     * 
     * @return 從實體轉換而來的ProductDto對象
     */
    @GetMapping("/entity-to-dto")
    @Operation(summary = "實體到DTO轉換", description = "演示如何將Product實體轉換為ProductDto，處理屬性重命名、格式轉換等", responses = {
            @ApiResponse(responseCode = "200", description = "成功轉換", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)))
    })
    public ProductDto getEntityToDto() {
        return productService.convertEntityToDto();
    }

    /**
     * DTO到實體轉換示例
     * 演示如何將ProductDto轉換為Product實體
     * 
     * 預期返回：
     * {
     * "id": 2,
     * "name": "智能手機",
     * "description": "最新智能手機，配備高清相機和長效電池",
     * "price": 799.99,
     * "sku": "PHONE-002",
     * "available": true,
     * "createdAt": null,
     * "updatedAt": "2023-11-20T15:35:45.123456"
     * }
     * 
     * @return 包含實體數據的Map
     */
    @GetMapping("/dto-to-entity")
    @Operation(summary = "DTO到實體轉換", description = "演示如何將ProductDto轉換為Product實體，處理反向映射和屬性轉換", responses = {
            @ApiResponse(responseCode = "200", description = "成功轉換", content = @Content(mediaType = "application/json"))
    })
    public Map<String, Object> getDtoToEntity() {
        Product product = productService.convertDtoToEntity();

        // 將實體轉換為 Map 以便於在 JSON 響應中顯示
        Map<String, Object> response = new HashMap<>();
        response.put("id", product.getId());
        response.put("name", product.getName());
        response.put("description", product.getDescription());
        response.put("price", product.getPrice());
        response.put("sku", product.getSku());
        response.put("available", product.isAvailable());
        response.put("createdAt", product.getCreatedAt());
        response.put("updatedAt", product.getUpdatedAt());

        return response;
    }

    /**
     * Map到DTO轉換示例
     * 演示如何從Map數據結構轉換為ProductDto
     * 
     * 預期返回：
     * {
     * "id": 3,
     * "name": "從Map創建的產品",
     * "description": "這是一個從Map創建的測試產品",
     * "price": 399.99,
     * "sku": "MAP-003",
     * "inStock": true,
     * "createdDate": "2023-11-20 15:40:30"
     * }
     * 
     * @return 從Map轉換而來的ProductDto對象
     */
    @GetMapping("/map-to-dto")
    @Operation(summary = "Map到DTO轉換", description = "演示如何從Map數據結構轉換為ProductDto，處理不同的數據類型和格式", responses = {
            @ApiResponse(responseCode = "200", description = "成功轉換", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)))
    })
    public ProductDto getMapToDto() {
        return productService.convertMapToDto();
    }

    /**
     * 更新實體示例
     * 演示如何使用DTO更新現有的實體對象
     * 
     * 預期返回：
     * {
     * "id": 4,
     * "name": "更新後的產品名稱",
     * "description": "更新後的產品描述",
     * "price": 129.99,
     * "sku": "OLD-001",
     * "available": true,
     * "createdAt": "2023-11-20T10:00:00",
     * "updatedAt": "2023-11-20T15:45:15.654321"
     * }
     * 
     * @return 包含更新後實體數據的Map
     */
    @GetMapping("/update-entity")
    @Operation(summary = "使用DTO更新實體", description = "演示如何使用DTO更新現有的實體對象，同時保留部分實體原值（如ID）", responses = {
            @ApiResponse(responseCode = "200", description = "成功更新", content = @Content(mediaType = "application/json"))
    })
    public Map<String, Object> updateEntity() {
        Product updatedProduct = productService.updateEntityFromDto();

        // 將更新後的實體轉換為 Map 以便於在 JSON 響應中顯示
        Map<String, Object> response = new HashMap<>();
        response.put("id", updatedProduct.getId());
        response.put("name", updatedProduct.getName());
        response.put("description", updatedProduct.getDescription());
        response.put("price", updatedProduct.getPrice());
        response.put("sku", updatedProduct.getSku());
        response.put("available", updatedProduct.isAvailable());
        response.put("createdAt", updatedProduct.getCreatedAt());
        response.put("updatedAt", updatedProduct.getUpdatedAt());

        return response;
    }
}