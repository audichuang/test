package com.example.mapstructdemo.controller;

import com.example.mapstructdemo.dto.ProductDto;
import com.example.mapstructdemo.mapper.MapStructExampleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * MapStruct示例控制器
 * 演示不同類型的Map結構如何通過MapStruct映射到DTO對象
 * 包含：
 * 1. 扁平Map結構轉換
 * 2. 嵌套複雜Map結構轉換
 * 3. 產品列表轉換
 * 4. 支持POST請求動態轉換不同類型的Map
 */
@RestController
@RequestMapping("/api/map-examples")
@Tag(name = "Map結構映射", description = "演示如何將不同結構的Map映射到DTO對象，包括扁平Map、嵌套Map和列表結構")
public class MapExampleController {

    private final MapStructExampleMapper mapStructExampleMapper;

    @Autowired
    public MapExampleController(MapStructExampleMapper mapStructExampleMapper) {
        this.mapStructExampleMapper = mapStructExampleMapper;
    }

    /**
     * 扁平Map結構轉換示例
     * 演示如何將一個扁平的Map(所有字段都在同一層級)轉換為ProductDto
     *
     * 預期返回：
     * {
     * "id": 101,
     * "name": "智能手機",
     * "description": "最新款智能手機，搭載高通驍龍處理器",
     * "price": 5999.99,
     * "sku": "PHONE-101",
     * "inStock": true,
     * "createdDate": "2023-05-15 14:30:45"
     * }
     *
     * @return 轉換後的ProductDto對象
     */
    @GetMapping("/flat-map")
    @Operation(summary = "扁平Map結構轉換", description = "演示如何將扁平的Map結構(所有字段在同一層級)轉換為ProductDto對象", responses = {
            @ApiResponse(responseCode = "200", description = "成功轉換", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)))
    })
    public ResponseEntity<ProductDto> flatMapExample() {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("id", 101);
        productMap.put("name", "智能手機");
        productMap.put("desc", "最新款智能手機，搭載高通驍龍處理器");
        productMap.put("price", 5999.99);
        productMap.put("code", "PHONE-101");
        productMap.put("in_stock", true);
        productMap.put("created_at", "2023-05-15T14:30:45");

        ProductDto productDto = mapStructExampleMapper.flatMapToProductDto(productMap);
        return ResponseEntity.ok(productDto);
    }

    /**
     * 複雜嵌套Map結構轉換示例
     * 演示如何將具有嵌套結構的Map轉換為ProductDto
     *
     * 例如以下結構:
     * {
     * "product": {
     * "id": 202,
     * "name": "筆記本電腦",
     * "description": "高性能商務筆記本",
     * "price": 8999.99,
     * "sku": "LAPTOP-202",
     * "available": true
     * },
     * "createTime": "2023-05-15T14:30:45"
     * }
     *
     * @return 轉換後的ProductDto對象
     */
    @GetMapping("/complex-map")
    @Operation(summary = "嵌套Map結構轉換", description = "演示如何將具有嵌套結構的Map轉換為ProductDto，其中產品信息被封裝在子Map中", responses = {
            @ApiResponse(responseCode = "200", description = "成功轉換", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)))
    })
    public ResponseEntity<ProductDto> complexMapExample() {
        Map<String, Object> complexMap = new HashMap<>();

        Map<String, Object> productDetails = new HashMap<>();
        productDetails.put("id", 202L);
        productDetails.put("name", "筆記本電腦");
        productDetails.put("description", "高性能商務筆記本，配備SSD硬盤");
        productDetails.put("price", new BigDecimal("8999.99"));
        productDetails.put("sku", "LAPTOP-202");
        productDetails.put("available", true);

        complexMap.put("product", productDetails);

        complexMap.put("createTime", LocalDateTime.now().toString());

        ProductDto productDto = mapStructExampleMapper.complexMapToProductDto(complexMap);
        return ResponseEntity.ok(productDto);
    }

    /**
     * 產品列表轉換示例
     * 演示如何從包含產品列表的Map中提取並轉換產品數據
     *
     * 預期處理的數據結構:
     * {
     * "products": [
     * {
     * "id": 301,
     * "name": "藍牙耳機",
     * "desc": "無線藍牙耳機",
     * "price": 399.99,
     * "code": "AUDIO-301",
     * "in_stock": true,
     * "created_at": 1623341730000
     * },
     * {
     * "id": 302,
     * "name": "智能手錶",
     * ...
     * }
     * ],
     * "total": 2,
     * "page": 1
     * }
     *
     * @return 轉換後的ProductDto列表
     */
    @GetMapping("/product-list")
    @Operation(summary = "產品列表轉換", description = "演示如何從包含產品列表的Map中提取並轉換產品數據，處理分頁結構的API響應", responses = {
            @ApiResponse(responseCode = "200", description = "成功轉換", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDto.class))))
    })
    public ResponseEntity<List<ProductDto>> productListExample() {
        Map<String, Object> apiResponse = new HashMap<>();

        List<Map<String, Object>> products = new ArrayList<>();

        Map<String, Object> product1 = new HashMap<>();
        product1.put("id", 301);
        product1.put("name", "藍牙耳機");
        product1.put("desc", "無線藍牙耳機，降噪功能");
        product1.put("price", 399.99);
        product1.put("code", "AUDIO-301");
        product1.put("in_stock", true);
        product1.put("created_at", System.currentTimeMillis());
        products.add(product1);

        Map<String, Object> product2 = new HashMap<>();
        product2.put("id", 302);
        product2.put("name", "智能手錶");
        product2.put("desc", "健康監測智能手錶");
        product2.put("price", 1299.50);
        product2.put("code", "WATCH-302");
        product2.put("in_stock", false);
        product2.put("created_at", "2023-06-10T09:15:30");
        products.add(product2);

        apiResponse.put("products", products);
        apiResponse.put("total", 2);
        apiResponse.put("page", 1);

        List<ProductDto> productDtos = mapStructExampleMapper.processProductsResponse(apiResponse);
        return ResponseEntity.ok(productDtos);
    }

    /**
     * 動態轉換不同結構的Map
     * 根據輸入Map的結構自動選擇適合的轉換方法
     *
     * 支持兩種格式:
     * 1. 扁平結構:
     * {
     * "id": 401,
     * "name": "智能音箱",
     * "desc": "高品質音質智能音箱",
     * "price": 999.99,
     * "code": "AUDIO-401",
     * "in_stock": true,
     * "created_at": "2023-08-15T10:30:00"
     * }
     *
     * 2. 嵌套結構:
     * {
     * "product": {
     * "id": 501,
     * "name": "智能手環",
     * "description": "健康監測智能手環",
     * "price": 599.99,
     * "sku": "WEARABLE-501",
     * "available": true
     * },
     * "createTime": "2023-09-20T14:25:00"
     * }
     *
     * @param customMap 客戶端發送的Map數據
     * @return 轉換後的ProductDto對象
     */
    @PostMapping("/convert")
    @Operation(summary = "動態轉換不同結構的Map", description = "根據輸入Map的結構自動選擇適合的轉換方法，支持扁平結構和嵌套結構", responses = {
            @ApiResponse(responseCode = "200", description = "成功轉換", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)))
    })
    public ResponseEntity<ProductDto> convertCustomMap(@RequestBody Map<String, Object> customMap) {
        ProductDto result;

        if (customMap.containsKey("product") && customMap.get("product") instanceof Map) {
            result = mapStructExampleMapper.complexMapToProductDto(customMap);
        } else {
            result = mapStructExampleMapper.flatMapToProductDto(customMap);
        }

        return ResponseEntity.ok(result);
    }
}