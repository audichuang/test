# MapStruct 在 Spring Boot 中的應用示例

這個項目演示了如何在 Spring Boot 應用中使用 MapStruct 進行對象映射，包括：

- Entity 轉換為 DTO
- DTO 轉換為 Entity
- Map 轉換為 DTO
- 部分更新 Entity

## 專案結構

```
src/main/java/com/example/mapstructdemo/
├── MapStructDemoApplication.java    # 應用程式入口
├── controller/
│   └── ProductController.java       # RESTful API 控制器
├── dto/
│   └── ProductDto.java              # 資料傳輸對象
├── entity/
│   └── Product.java                 # 實體類
├── mapper/
│   └── ProductMapper.java           # MapStruct 映射接口
└── service/
    └── ProductService.java          # 業務邏輯服務
```

## MapStruct 關鍵特性

這個示例展示了 MapStruct 的以下特性：

1. **基本映射** - 在不同對象之間自動映射相同名稱的屬性
2. **自定義映射** - 使用 `@Mapping` 註解處理不同名稱的屬性
3. **類型轉換** - 自動處理兼容類型（如 Long 和 Integer）
4. **複雜類型轉換** - 自定義方法處理特殊類型（如 LocalDateTime 轉 String）
5. **命名方法** - 使用 `@Named` 創建可重用的轉換方法
6. **表達式** - 使用 Java 表達式動態生成值
7. **忽略屬性** - 使用 `ignore = true` 跳過某些屬性
8. **更新現有對象** - 使用 `@MappingTarget` 更新已存在的對象
9. **Map 轉換** - 從 Map 結構轉換到普通對象

## API 端點

啟動應用後，可以通過以下端點測試不同類型的轉換：

- `GET /api/products/entity-to-dto` - 將 Product 實體轉換為 ProductDto
- `GET /api/products/dto-to-entity` - 將 ProductDto 轉換為 Product 實體
- `GET /api/products/map-to-dto` - 將 Map 轉換為 ProductDto
- `GET /api/products/update-entity` - 使用 DTO 部分更新實體

## 如何運行

1. 克隆此存儲庫
2. 執行 `mvn clean install` 編譯項目
3. 執行 `mvn spring-boot:run` 啟動應用
4. 訪問 `http://localhost:8080/api/products/entity-to-dto` 等端點測試

## 關於 MapStruct

MapStruct 是一個代碼生成器，可以大大簡化 Java Bean 之間的映射實現。與其他映射框架不同，MapStruct 在編譯時生成映射代碼，這意味著：

- 快速執行，無反射開銷
- 編譯時類型安全
- 易於調試
- 清晰的錯誤報告

MapStruct 在編譯期間會生成 `ProductMapperImpl.java` 實現類，這個類包含了所有映射邏輯的具體實現。

## 生成的實現類示意

當構建項目時，MapStruct 處理器會生成類似以下的實現代碼：

```java
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto productToProductDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDtoBuilder productDto = ProductDto.builder();

        productDto.id(product.getId());
        productDto.name(product.getName());
        productDto.description(product.getDescription());
        productDto.price(product.getPrice());
        productDto.sku(product.getSku());
        productDto.inStock(product.isAvailable());
        productDto.createdDate(formatDateTime(product.getCreatedAt()));

        return productDto.build();
    }

    // 其他方法的實現...
}
```
