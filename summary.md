# MapStruct 使用總結

## 什麼是 MapStruct？

MapStruct 是一個 Java Bean 映射框架，它通過代碼生成方式實現高效且類型安全的對象映射。與其他映射框架（如 ModelMapper 或 Dozer）不同，MapStruct 在編譯時生成映射代碼，而不是在運行時使用反射，這使得它的執行速度非常快。

## MapStruct 的優勢

1. **性能高效**：

   - 編譯時生成代碼，無反射開銷
   - 執行速度比基於反射的解決方案快約 25 倍

2. **類型安全**：

   - 在編譯時檢查類型錯誤
   - 明確的錯誤報告，而不是運行時異常

3. **易於使用**：

   - 使用註解驅動的聲明式 API
   - 自動匹配同名屬性
   - 支持嵌套對象的映射

4. **高度可定制**：

   - 自定義映射邏輯
   - 支持繼承和複用映射
   - 可以處理特殊轉換案例

5. **易於調試**：

   - 生成的映射代碼清晰可讀
   - 可以像普通 Java 代碼一樣進行調試

6. **與框架集成**：
   - 無縫集成 Spring 框架
   - 支持 CDI 依賴注入

## MapStruct 常用註解

| 注解                           | 說明                                             |
| ------------------------------ | ------------------------------------------------ |
| `@Mapper`                      | 標記一個接口為映射接口，MapStruct 會為其生成實現 |
| `@Mapping`                     | 配置源對象和目標對象之間的屬性映射               |
| `@MappingTarget`               | 標記一個參數為映射的目標，用於更新現有對象       |
| `@Named`                       | 創建一個命名的映射方法，可被其他映射引用         |
| `@BeanMapping`                 | 配置整個 bean 的映射行為                         |
| `@InheritConfiguration`        | 繼承其他方法的映射配置                           |
| `@InheritInverseConfiguration` | 繼承其他方法的反向映射配置                       |
| `@AfterMapping`                | 標記一個方法在映射完成後執行                     |
| `@BeforeMapping`               | 標記一個方法在映射開始前執行                     |

## @Mapping 常用屬性

| 屬性              | 說明                                 |
| ----------------- | ------------------------------------ |
| `source`          | 源對象的屬性名                       |
| `target`          | 目標對象的屬性名                     |
| `qualifiedByName` | 引用 `@Named` 標記的方法進行特殊轉換 |
| `expression`      | 使用 Java 表達式生成值               |
| `ignore`          | 是否忽略該屬性的映射                 |
| `defaultValue`    | 源屬性為 null 時的默認值             |
| `dateFormat`      | 日期格式化模式                       |
| `numberFormat`    | 數字格式化模式                       |

## @Mapper 常用屬性

| 屬性                               | 說明                                                        |
| ---------------------------------- | ----------------------------------------------------------- |
| `componentModel`                   | 生成的映射器組件模型（"default", "spring", "cdi", "jsr330") |
| `uses`                             | 使用其他映射器進行複合映射                                  |
| `imports`                          | 導入靜態方法或常量                                          |
| `unmappedTargetPolicy`             | 未映射目標屬性的處理策略                                    |
| `nullValueMappingStrategy`         | null 值映射策略                                             |
| `nullValuePropertyMappingStrategy` | null 屬性值映射策略                                         |
| `collectionMappingStrategy`        | 集合映射策略                                                |

## MapStruct 使用步驟

1. **添加依賴**：

   ```xml
   <dependency>
       <groupId>org.mapstruct</groupId>
       <artifactId>mapstruct</artifactId>
       <version>${org.mapstruct.version}</version>
   </dependency>
   <dependency>
       <groupId>org.mapstruct</groupId>
       <artifactId>mapstruct-processor</artifactId>
       <version>${org.mapstruct.version}</version>
       <scope>provided</scope>
   </dependency>
   ```

2. **創建映射接口**：

   ```java
   @Mapper(componentModel = "spring")
   public interface ProductMapper {
       ProductDto productToProductDto(Product product);
       Product productDtoToProduct(ProductDto productDto);
   }
   ```

3. **配置自定義映射**：

   ```java
   @Mapping(source = "available", target = "inStock")
   @Mapping(source = "createdAt", target = "createdDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
   ProductDto productToProductDto(Product product);
   ```

4. **注入並使用映射器**：

   ```java
   @Service
   public class ProductService {
       private final ProductMapper productMapper;

       @Autowired
       public ProductService(ProductMapper productMapper) {
           this.productMapper = productMapper;
       }

       public ProductDto getProductDto(Product product) {
           return productMapper.productToProductDto(product);
       }
   }
   ```

## MapStruct 最佳實踐

1. **使用 Spring 集成**：

   - 設置 `@Mapper(componentModel = "spring")`
   - 這樣映射器可以直接通過 `@Autowired` 注入

2. **處理特殊類型轉換**：

   - 創建自定義方法處理複雜轉換
   - 使用 `@Named` 使方法可重用

3. **利用繼承**：

   - 創建通用基礎映射接口
   - 使用 `@InheritConfiguration` 繼承配置

4. **避免使用映射表達式**：

   - 當可能時，優先使用自定義方法
   - 表達式較難維護和測試

5. **為集合處理設置策略**：

   - 適當選擇 `collectionMappingStrategy`
   - 根據需求使用 `ACCESSOR_ONLY`、`SETTER_PREFERRED` 或 `ADDER_PREFERRED`

6. **正確處理 null 值**：

   - 配置 `nullValueMappingStrategy`
   - 針對特定屬性設置 `defaultValue`

7. **使用 `@MappingTarget` 更新現有對象**：

   - 避免不必要的對象創建
   - 實現部分更新

8. **為未映射的屬性設置策略**：
   - 使用 `unmappedTargetPolicy = ReportingPolicy.WARN` 或 `ERROR`
   - 確保意識到未映射的屬性

## 與其他映射框架比較

| 框架            | 優點                               | 缺點                           |
| --------------- | ---------------------------------- | ------------------------------ |
| **MapStruct**   | 編譯時生成代碼，高性能，類型安全   | 需要編譯時處理，配置略顯冗長   |
| **ModelMapper** | 靈活，約定優於配置，運行時自動映射 | 基於反射，性能較低，運行時錯誤 |
| **Dozer**       | 功能全面，XML 或 API 配置          | 性能最差，配置複雜             |
| **JMapper**     | 基於註解，性能較好                 | 文檔較少，社區較小             |
| **Orika**       | 基於字節碼生成，性能較好           | 配置複雜，學習曲線陡峭         |

## 結論

MapStruct 是一個出色的對象映射框架，特別適合企業應用程序中的 DTO 轉換場景。它提供了編譯時安全性、高性能和優秀的開發體驗的獨特組合。通過自動生成明確、類型安全的映射代碼，MapStruct 不僅能提高應用程序性能，還能減少錯誤和簡化維護。

對於任何需要在不同對象模型之間進行轉換的 Java 應用程序，MapStruct 都是一個值得考慮的解決方案，尤其是當性能和類型安全是關鍵要求時。
