# MapStruct 詳細使用手冊

## 目錄

1. [基本概念與原理](#1-基本概念與原理)
2. [環境配置](#2-環境配置)
3. [基本映射操作](#3-基本映射操作)
4. [高級映射技巧](#4-高級映射技巧)
5. [集成與最佳實踐](#5-集成與最佳實踐)
6. [MapStruct 實現原理](#6-mapstruct-實現原理)
7. [故障排除與常見問題](#7-故障排除與常見問題)
8. [實戰示例](#8-實戰示例)
9. [參考資源](#9-參考資源)

## 1. 基本概念與原理

### 1.1 什麼是 MapStruct？

MapStruct 是一個 Java 註解處理器，用於生成類型安全、高性能的 Java Bean 映射代碼。它基於約定優於配置的原則，只需要定義映射接口，MapStruct 就會在編譯時自動生成實現類。

### 1.2 為什麼選擇 MapStruct？

- **性能優越**：編譯時生成代碼，避免了運行時反射帶來的性能開銷
- **類型安全**：編譯時檢查，避免運行時映射錯誤
- **開發效率**：減少手動編寫重複的映射代碼
- **可讀性和可維護性**：生成的代碼清晰可讀
- **靈活性**：支持複雜映射和自定義邏輯

### 1.3 核心概念

- **映射器 (Mapper)**：定義如何從源對象轉換到目標對象的接口
- **映射方法 (Mapping Methods)**：在映射器中定義的方法，指定轉換邏輯
- **屬性映射 (Property Mappings)**：源對象和目標對象之間字段的對應關係
- **自定義映射 (Custom Mappings)**：處理特殊轉換邏輯的方法

### 1.4 MapStruct 與其他框架對比

| 框架            | 優點                               | 缺點                           |
| --------------- | ---------------------------------- | ------------------------------ |
| **MapStruct**   | 編譯時生成代碼，高性能，類型安全   | 需要編譯時處理，配置略顯冗長   |
| **ModelMapper** | 靈活，約定優於配置，運行時自動映射 | 基於反射，性能較低，運行時錯誤 |
| **Dozer**       | 功能全面，XML 或 API 配置          | 性能最差，配置複雜             |
| **JMapper**     | 基於註解，性能較好                 | 文檔較少，社區較小             |
| **Orika**       | 基於字節碼生成，性能較好           | 配置複雜，學習曲線陡峭         |

## 2. 環境配置

### 2.1 Maven 配置

```xml
<properties>
    <org.mapstruct.version>1.5.5.Final</org.mapstruct.version>
</properties>

<dependencies>
    <!-- MapStruct 核心 -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${org.mapstruct.version}</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <annotationProcessorPaths>
                    <!-- MapStruct 處理器 -->
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${org.mapstruct.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 2.2 Gradle 配置

```groovy
plugins {
    id 'java'
}

dependencies {
    implementation 'org.mapstruct:mapstruct:1.5.5.Final'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.5.Final'
}
```

### 2.3 與 Lombok 集成

如果使用 Lombok，需要特殊配置以確保 MapStruct 正確處理 Lombok 生成的代碼：

#### Maven

```xml
<annotationProcessorPaths>
    <path>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${org.mapstruct.version}</version>
    </path>
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
    </path>
    <!-- 這個處理器結合了 Lombok 和 MapStruct -->
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok-mapstruct-binding</artifactId>
        <version>0.2.0</version>
    </path>
</annotationProcessorPaths>
```

#### Gradle

```groovy
dependencies {
    implementation 'org.mapstruct:mapstruct:1.5.5.Final'
    implementation 'org.projectlombok:lombok:1.18.30'

    annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.5.Final'
    annotationProcessor 'org.projectlombok:lombok:1.18.30'
    annotationProcessor 'org.projectlombok:lombok-mapstruct-binding:0.2.0'
}
```

### 2.4 IDE 配置

#### IntelliJ IDEA

確保 Annotation Processing 已啟用:

1. 進入 `Settings` > `Build, Execution, Deployment` > `Compiler` > `Annotation Processors`
2. 選中 `Enable annotation processing`
3. 可選：配置生成的源碼目錄為 `target/generated-sources/annotations`

#### Eclipse

1. 安裝 `m2e-apt` 插件
2. 右鍵項目 > `Properties` > `Maven` > `Annotation Processing` > 選中 `Enable annotation processing`

## 3. 基本映射操作

### 3.1 創建基本映射器

首先創建兩個需要互相映射的實體類和 DTO：

```java
// 實體類
public class UserEntity {
    private Long id;
    private String username;
    private String email;
    private LocalDate birthDate;
    private boolean active;
    // getters and setters
}

// DTO
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String birthDateString;
    private boolean enabled;
    // getters and setters
}
```

然後定義映射接口：

```java
@Mapper
public interface UserMapper {

    // Entity → DTO
    @Mapping(source = "birthDate", target = "birthDateString", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "active", target = "enabled")
    UserDto userToUserDto(UserEntity user);

    // DTO → Entity
    @Mapping(source = "birthDateString", target = "birthDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "enabled", target = "active")
    UserEntity userDtoToUser(UserDto userDto);
}
```

### 3.2 使用生成的映射器

編譯後，MapStruct 會生成 `UserMapperImpl` 類。要使用它：

```java
// 標準 Java 方式
UserMapper mapper = new UserMapperImpl();

// 使用 Spring 方式 (需要設置 componentModel = "spring")
@Autowired
private UserMapper mapper;

// 實際使用
UserEntity user = new UserEntity();
user.setId(1L);
user.setUsername("johndoe");
user.setEmail("john@example.com");
user.setBirthDate(LocalDate.of(1990, 1, 1));
user.setActive(true);

// 轉換為 DTO
UserDto dto = mapper.userToUserDto(user);
```

### 3.3 多源映射

從多個源對象映射到一個目標對象：

```java
@Mapper
public interface AddressMapper {

    @Mapping(source = "person.firstName", target = "firstName")
    @Mapping(source = "person.lastName", target = "lastName")
    @Mapping(source = "address.street", target = "street")
    @Mapping(source = "address.city", target = "city")
    ContactDto personAndAddressToContact(Person person, Address address);
}
```

### 3.4 集合映射

MapStruct 自動處理集合映射：

```java
@Mapper
public interface OrderMapper {

    OrderDto orderToOrderDto(Order order);

    // 自動映射列表中的每個元素
    List<OrderDto> ordersToOrderDtos(List<Order> orders);

    // 自動映射 Map 中的值
    Map<String, OrderDto> orderMapToOrderDtoMap(Map<String, Order> orderMap);
}
```

### 3.5 基本類型轉換

MapStruct 可以自動處理兼容的基本類型轉換：

```java
@Mapper
public interface NumberMapper {

    @Mapping(source = "intValue", target = "longValue")
    @Mapping(source = "doubleValue", target = "floatValue")
    @Mapping(source = "stringValue", target = "integerValue")
    TargetBean convert(SourceBean source);
}
```

這將自動處理:

- int → long
- double → float
- String → Integer (使用 Integer.parseInt)

## 4. 高級映射技巧

### 4.1 自定義類型轉換

當標準映射不足時，可以定義自定義方法：

```java
@Mapper
public interface DateMapper {

    @Mapping(source = "timestamp", target = "date", qualifiedByName = "timestampToDate")
    EventDto eventToEventDto(Event event);

    @Named("timestampToDate")
    default String timestampToDate(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
```

### 4.2 條件映射

根據條件決定是否執行某些映射：

```java
@Mapper
public interface DocumentMapper {

    @Mapping(target = "confidential", constant = "true",
             condition = "java(document.getType() == DocumentType.CLASSIFIED)")
    @Mapping(target = "summary", expression = "java(generateSummary(document))")
    DocumentDto documentToDocumentDto(Document document);

    default String generateSummary(Document document) {
        // 生成摘要的邏輯
        return document.getContent().substring(0,
                Math.min(100, document.getContent().length())) + "...";
    }
}
```

### 4.3 更新現有對象

使用 `@MappingTarget` 更新現有對象而不是創建新對象：

```java
@Mapper
public interface ProductMapper {

    @Mapping(target = "id", ignore = true) // 不更新 ID
    void updateProductFromDto(ProductDto dto, @MappingTarget Product product);
}
```

然後使用：

```java
// 在服務層
Product existingProduct = productRepository.findById(id).orElseThrow();
mapper.updateProductFromDto(productDto, existingProduct);
productRepository.save(existingProduct);
```

### 4.4 嵌套映射

映射嵌套對象：

```java
@Mapper
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(Customer customer);

    AddressDto addressToAddressDto(Address address);
}
```

如果 `Customer` 包含 `Address`，MapStruct 會自動使用 `addressToAddressDto` 方法映射嵌套屬性。

### 4.5 繼承和複合映射

使用其他映射器：

```java
@Mapper(uses = {AddressMapper.class, OrderMapper.class})
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(Customer customer);

    // AddressMapper 會處理這個映射
    // OrderMapper 會處理訂單列表的映射
}
```

### 4.6 映射前後處理

在映射前後執行代碼：

```java
@Mapper
public abstract class AuditMapper {

    @BeforeMapping
    protected void logBeforeMapping(Object source, @MappingTarget Object target) {
        System.out.println("開始映射: " + source.getClass() + " → " + target.getClass());
    }

    @AfterMapping
    protected void logAfterMapping(Object source, @MappingTarget Object target) {
        System.out.println("完成映射: " + target);
    }

    public abstract AuditLogDto auditLogToDto(AuditLog log);
}
```

### 4.7 Map 結構映射

從 Map 到對象的映射：

```java
@Mapper
public interface MapMapper {

    @Mapping(source = "user_id", target = "id")
    @Mapping(source = "user_name", target = "name")
    @Mapping(source = "user_email", target = "email")
    UserDto mapToUser(Map<String, Object> map);

    default Long map(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
```

#### 4.7.1 處理複雜 Map 結構

在實務中，我們經常需要處理各種複雜的 Map 結構，例如來自 API 響應、JSON 數據或動態數據源：

```java
@Mapper(componentModel = "spring")
public interface ComplexMapMapper {

    // 處理扁平結構的Map
    @Mapping(source = "id", target = "id", qualifiedByName = "mapToLong")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "price", target = "price", qualifiedByName = "mapToBigDecimal")
    @Mapping(source = "in_stock", target = "available", qualifiedByName = "mapToBoolean")
    @Mapping(source = "created_at", target = "creationDate", qualifiedByName = "stringToDate")
    ProductDto flatMapToProduct(Map<String, Object> productMap);

    // 處理嵌套結構的Map
    @Mapping(source = "product.id", target = "id", qualifiedByName = "mapToLong")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "metadata.createdOn", target = "creationDate", qualifiedByName = "stringToDate")
    ProductDto nestedMapToProduct(Map<String, Object> dataMap);

    // 處理Map列表
    default List<ProductDto> mapProductsList(List<Map<String, Object>> productMaps) {
        if (productMaps == null) {
            return Collections.emptyList();
        }
        return productMaps.stream()
                .map(this::flatMapToProduct)
                .collect(Collectors.toList());
    }

    // 從複雜的API響應中提取產品列表
    default List<ProductDto> extractProductsFromResponse(Map<String, Object> response) {
        if (response == null || !response.containsKey("data")) {
            return Collections.emptyList();
        }

        try {
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            List<Map<String, Object>> products = (List<Map<String, Object>>) data.get("products");
            return mapProductsList(products);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // 類型轉換方法
    @Named("mapToLong")
    default Long mapToLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Named("mapToBigDecimal")
    default BigDecimal mapToBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }
        if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Named("mapToBoolean")
    default boolean mapToBoolean(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            String str = (String) value;
            return "true".equalsIgnoreCase(str) ||
                   "yes".equalsIgnoreCase(str) ||
                   "1".equals(str);
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        return false;
    }

    @Named("stringToDate")
    default String stringToDate(Object value) {
        if (value == null) {
            return null;
        }
        // 處理不同格式的日期
        if (value instanceof String) {
            try {
                // 嘗試ISO格式
                LocalDateTime dateTime = LocalDateTime.parse(
                        (String) value, DateTimeFormatter.ISO_DATE_TIME);
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception e) {
                // 回傳原始字符串
                return (String) value;
            }
        } else if (value instanceof Number) {
            // 處理時間戳
            try {
                LocalDateTime dateTime = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(((Number) value).longValue()),
                        ZoneId.systemDefault());
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception e) {
                return null;
            }
        }
        return value.toString();
    }
}
```

#### 4.7.2 實際應用場景

以下是一些使用 Map 映射的實際應用場景：

1. **處理動態 API 響應**：

   ```java
   @Service
   public class ProductService {
       private final ComplexMapMapper mapper;

       @Autowired
       public ProductService(ComplexMapMapper mapper) {
           this.mapper = mapper;
       }

       public List<ProductDto> fetchProducts() {
           Map<String, Object> apiResponse = apiClient.fetchProducts();
           return mapper.extractProductsFromResponse(apiResponse);
       }
   }
   ```

2. **處理 JSON 數據**：

   ```java
   @RestController
   @RequestMapping("/api/products")
   public class ProductController {
       private final ComplexMapMapper mapper;

       @Autowired
       public ProductController(ComplexMapMapper mapper) {
           this.mapper = mapper;
       }

       @PostMapping("/convert")
       public ResponseEntity<ProductDto> convertProduct(@RequestBody Map<String, Object> productData) {
           ProductDto dto = mapper.flatMapToProduct(productData);
           return ResponseEntity.ok(dto);
       }

       @PostMapping("/convert-nested")
       public ResponseEntity<ProductDto> convertNestedProduct(@RequestBody Map<String, Object> nestedData) {
           ProductDto dto = mapper.nestedMapToProduct(nestedData);
           return ResponseEntity.ok(dto);
       }
   }
   ```

3. **處理數據庫結果**：

   ```java
   @Repository
   public class ProductRepository {
       private final JdbcTemplate jdbcTemplate;
       private final ComplexMapMapper mapper;

       @Autowired
       public ProductRepository(JdbcTemplate jdbcTemplate, ComplexMapMapper mapper) {
           this.jdbcTemplate = jdbcTemplate;
           this.mapper = mapper;
       }

       public List<ProductDto> findAllProducts() {
           String sql = "SELECT * FROM products";
           List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
           return mapper.mapProductsList(rows);
       }
   }
   ```

#### 4.7.3 Map 映射的最佳實踐

處理 Map 映射時的建議：

1. **類型安全**：始終使用命名的轉換方法處理不確定類型的值
2. **空值處理**：總是檢查 null 值，避免 NullPointerException
3. **錯誤處理**：添加適當的異常處理，確保映射過程的穩健性
4. **可讀性**：通過方法命名和註釋清楚地說明每個映射的用途
5. **模塊性**：將通用的映射方法抽取到基礎映射器中重用
6. **穩健設計**：設計映射器時考慮輸入數據的各種可能格式

### 4.8 表達式映射

使用 Java 表達式動態生成值：

```java
@Mapper
public interface OrderMapper {

    @Mapping(target = "fullName",
            expression = "java(order.getFirstName() + \" \" + order.getLastName())")
    @Mapping(target = "totalAmount",
            expression = "java(calculateTotal(order.getItems()))")
    OrderSummary orderToSummary(Order order);

    default BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

### 4.9 反向映射

使用一個映射方法的反向配置：

```java
@Mapper
public interface VehicleMapper {

    @Mapping(source = "manufacturer", target = "make")
    @Mapping(source = "seats", target = "numSeats")
    CarDto carToCarDto(Car car);

    @InheritInverseConfiguration
    Car carDtoToCar(CarDto carDto);
}
```

## 5. 集成與最佳實踐

### 5.1 與 Spring 集成

使用 Spring 組件模型：

```java
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);
}
```

然後在任何 Spring 組件中注入：

```java
@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserDto getUserDto(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return userMapper.userToUserDto(user);
    }
}
```

### 5.2 全局配置

創建一個基礎映射器配置類：

```java
@MapperConfig(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface CentralConfig {
}
```

然後在其他映射器中使用：

```java
@Mapper(config = CentralConfig.class)
public interface UserMapper {
    // ...
}
```

### 5.3 繼承映射配置

使用繼承重用映射配置：

```java
@Mapper
public interface BaseMapper<E, D> {

    D toDto(E entity);

    E toEntity(D dto);
}

@Mapper(config = CentralConfig.class)
public interface UserMapper extends BaseMapper<User, UserDto> {

    @Override
    @Mapping(source = "birthDate", target = "birthDateString", dateFormat = "yyyy-MM-dd")
    UserDto toDto(User entity);

    @Override
    @InheritInverseConfiguration
    User toEntity(UserDto dto);
}
```

### 5.4 映射策略配置

配置映射策略：

```java
@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ConfiguredMapper {
    // ...
}
```

### 5.5 單元測試

測試映射器：

```java
class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void shouldMapUserToUserDto() {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setActive(true);
        user.setBirthDate(LocalDate.of(1990, 1, 1));

        // when
        UserDto dto = userMapper.userToUserDto(user);

        // then
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUsername()).isEqualTo("test");
        assertThat(dto.isEnabled()).isTrue();
        assertThat(dto.getBirthDateString()).isEqualTo("1990-01-01");
    }
}
```

## 6. MapStruct 實現原理

### 6.1 註解處理機制

MapStruct 基於 Java 的註解處理器 (Annotation Processor) 機制工作：

1. 當 Java 編譯器編譯源代碼時，它首先查找並執行所有註冊的註解處理器
2. MapStruct 處理器 (`org.mapstruct.ap.MappingProcessor`) 尋找帶有 `@Mapper` 註解的接口
3. 處理器分析接口中的方法，處理所有映射註解
4. 然後生成實現類，包含所有映射邏輯的具體實現
5. 生成的代碼像手寫的代碼一樣高效，但沒有人工錯誤

### 6.2 代碼生成流程

MapStruct 處理器的工作流程：

1. **掃描階段**：識別所有 `@Mapper` 接口和方法
2. **解析階段**：分析源類型和目標類型的結構，建立屬性映射關係
3. **驗證階段**：檢查映射的有效性，確認所有必需的映射都存在
4. **生成階段**：生成映射實現類的 Java 源代碼
5. **編譯階段**：Java 編譯器編譯生成的源代碼

### 6.3 生成代碼示例

假設有以下映射器定義：

```java
@Mapper
public interface SimpleMapper {
    @Mapping(source = "name", target = "fullName")
    PersonDto personToPersonDto(Person person);
}
```

MapStruct 生成的實現大致如下：

```java
public class SimpleMapperImpl implements SimpleMapper {

    @Override
    public PersonDto personToPersonDto(Person person) {
        if (person == null) {
            return null;
        }

        PersonDto personDto = new PersonDto();

        if (person.getName() != null) {
            personDto.setFullName(person.getName());
        }
        personDto.setAge(person.getAge());
        personDto.setEmail(person.getEmail());

        return personDto;
    }
}
```

關鍵特點：

- 生成純 Java 代碼，無反射
- 包含 null 檢查
- 直接調用 getter/setter 方法
- 處理自定義映射邏輯

### 6.4 性能考量

MapStruct 生成的代碼與手寫代碼一樣高效：

1. **無運行時開銷**：編譯時生成代碼，無反射，無代理
2. **直接方法調用**：直接調用 getter/setter 方法
3. **適當的空檢查**：只在必要時執行空檢查
4. **內聯簡單轉換**：簡單轉換直接內聯
5. **重用轉換邏輯**：複雜轉換被提取到助手方法

## 7. 故障排除與常見問題

### 7.1 常見編譯錯誤

**找不到映射方法**

```
Can't map property "X" to "Y". Consider to declare/implement a mapping method.
```

解決方案：

- 為特殊類型定義自定義映射方法
- 確保源字段和目標字段的類型兼容
- 使用 `qualifiedByName` 指定使用哪個映射方法

**缺少目標屬性的 setter**

```
No write accessor for property "X" in target type.
```

解決方案：

- 確保目標類有適當的 setter 方法
- 如果使用 Lombok，確保配置了 Lombok 處理器
- 使用 `target = "..."` 指定正確的目標屬性名

### 7.2 運行時問題

**找不到生成的實現類**

解決方案：

- 確保註解處理器正確配置在 Maven/Gradle 中
- 檢查 IDE 是否已啟用註解處理
- 檢查生成的代碼所在目錄是否已添加到類路徑

**Spring 無法注入映射器**

解決方案：

- 確保設置了 `@Mapper(componentModel = "spring")`
- 檢查 Spring 是否掃描了包含映射器的包
- 檢查生成的實現類是否被正確註冊為 Bean

### 7.3 常見陷阱

**循環依賴**

當兩個映射器相互使用時，可能出現循環依賴：

解決方案：

- 使用 `@Context` 打破循環
- 創建一個包含兩者共同方法的單獨映射器
- 將共享方法抽取到基礎映射器

**忽略 null 值**

默認情況下，MapStruct 總是映射 null 值：

解決方案：

- 設置 `nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE`
- 使用自定義 `@BeforeMapping` 方法處理 null 值

**集合映射問題**

解決方案：

- 確保使用兼容的集合類型
- 設置正確的 `collectionMappingStrategy`
- 手動處理特殊集合類型轉換

## 8. 實戰示例

### 8.1 複雜對象映射

**帶嵌套對象的實體類**

```java
@Mapper(uses = {AddressMapper.class, OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "customer.firstName", target = "customerFirstName")
    @Mapping(source = "customer.lastName", target = "customerLastName")
    @Mapping(source = "billingAddress", target = "billingAddress")
    @Mapping(source = "shippingAddress", target = "shippingAddress")
    @Mapping(target = "totalAmount", expression = "java(calculateTotal(order.getItems()))")
    OrderDto orderToOrderDto(Order order);

    default BigDecimal calculateTotal(List<OrderItem> items) {
        if (items == null) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

### 8.2 雙向映射與循環引用

```java
@Mapper
public interface DepartmentMapper {

    @Mapping(target = "employees", qualifiedByName = "employeesToDtos")
    DepartmentDto departmentToDto(Department department);

    @Named("employeesToDtos")
    default List<EmployeeDto> employeesToDtos(List<Employee> employees) {
        if (employees == null) {
            return null;
        }

        return employees.stream()
                .map(this::employeeToDto)
                .collect(Collectors.toList());
    }

    @Mapping(target = "department", ignore = true)
    EmployeeDto employeeToDto(Employee employee);

    // 反向映射
    @InheritInverseConfiguration
    Department dtoToDepartment(DepartmentDto dto);
}
```

### 8.3 處理繼承關係

```java
@Mapper
public interface VehicleMapper {

    @SubclassMapping(source = Car.class, target = CarDto.class)
    @SubclassMapping(source = Truck.class, target = TruckDto.class)
    VehicleDto vehicleToDto(Vehicle vehicle);

    CarDto carToDto(Car car);
    TruckDto truckToDto(Truck truck);

    @InheritInverseConfiguration
    Vehicle dtoToVehicle(VehicleDto dto);
}
```

### 8.4 從 JSON 映射到對象

```java
@Mapper
public interface JsonMapper {

    @Mapping(target = "birthDate", source = "birthDateStr",
             dateFormat = "yyyy-MM-dd")
    User jsonToUser(JsonNode jsonNode);

    default String map(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.get(fieldName).asText() : null;
    }

    default Long mapToLong(JsonNode node, String fieldName) {
        if (!node.has(fieldName) || node.get(fieldName).isNull()) {
            return null;
        }
        return node.get(fieldName).asLong();
    }
}
```

## 9. 參考資源

### 9.1 官方文檔

- [MapStruct 參考指南](https://mapstruct.org/documentation/stable/reference/html/)
- [MapStruct JavaDoc](https://mapstruct.org/documentation/stable/api/)
- [MapStruct GitHub 倉庫](https://github.com/mapstruct/mapstruct)

### 9.2 重要註解一覽

| 註解                           | 說明                                             |
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
| `@MapperConfig`                | 定義可被多個映射器共享的配置                     |
| `@Context`                     | 標記一個參數為映射上下文                         |
| `@SubclassMapping`             | 配置子類到子類的映射                             |

### 9.3 示例項目

[GitHub 示例](https://github.com/mapstruct/mapstruct-examples) 包含:

- 與各種框架集成
- 各種映射技巧
- 單元測試示例
- 性能比較

### 9.4 社區資源

- [Stack Overflow MapStruct 標籤](https://stackoverflow.com/questions/tagged/mapstruct)
- [MapStruct Gitter 聊天](https://gitter.im/mapstruct/mapstruct-users)
- [MapStruct 官方博客](https://mapstruct.org/news/)

## 處理多層嵌套 Map 結構映射到多層嵌套對象

在實際應用中，我們經常需要處理具有多層嵌套結構的數據，例如從第三方 API 返回的複雜 JSON 結構，或者是層級化的配置數據。在這種情況下，我們需要將這些多層嵌套的 Map 結構映射到同樣具有多層嵌套結構的對象中。

### 問題描述

假設我們有以下多層嵌套的 Map 結構：

```json
{
  "id": 3001,
  "name": "王五",
  "email": "wang@example.com",
  "phone": "555-123-4567",
  "address": {
    "street": "中山路45號",
    "building": {
      "floor": 5,
      "unit": "B"
    },
    "city": "高雄市",
    "zipCode": "800",
    "country": "台灣"
  }
}
```

我們需要將這個結構映射到同樣具有多層嵌套的對象結構：

```java
public class CustomerDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private AddressDto address;
}

public class AddressDto {
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private BuildingDto building;
}

public class BuildingDto {
    private Integer floor;
    private String unit;
}
```

### 解決方案

要處理這種多層嵌套結構，我們需要使用遞歸方法來處理每一層嵌套：

```java
@Mapper(componentModel = "spring")
public interface NestedMapMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "mapToLong")
    @Mapping(target = "name", source = "name", qualifiedByName = "mapToString")
    @Mapping(target = "email", source = "email", qualifiedByName = "mapToString")
    @Mapping(target = "phone", source = "phone", qualifiedByName = "mapToString")
    @Mapping(target = "address", expression = "java(mapToAddress(customerMap))")
    CustomerDto mapToCustomerDto(Map<String, Object> customerMap);

    // 第一層嵌套：處理地址對象
    default AddressDto mapToAddress(Map<String, Object> customerMap) {
        if (customerMap == null || !customerMap.containsKey("address")) {
            return null;
        }

        Object addressObj = customerMap.get("address");
        if (!(addressObj instanceof Map)) {
            return null;
        }

        Map<String, Object> addressMap = (Map<String, Object>) addressObj;

        AddressDto addressDto = new AddressDto();

        addressDto.setStreet(mapToString(addressMap.get("street")));
        addressDto.setCity(mapToString(addressMap.get("city")));
        addressDto.setZipCode(mapToString(addressMap.get("zipCode")));
        addressDto.setCountry(mapToString(addressMap.get("country")));

        // 處理第二層嵌套：建築對象
        if (addressMap.containsKey("building")) {
            addressDto.setBuilding(mapToBuilding(addressMap.get("building")));
        }

        return addressDto;
    }

    // 第二層嵌套：處理建築對象
    default BuildingDto mapToBuilding(Object buildingObj) {
        if (buildingObj == null || !(buildingObj instanceof Map)) {
            return null;
        }

        Map<String, Object> buildingMap = (Map<String, Object>) buildingObj;
        BuildingDto buildingDto = new BuildingDto();

        if (buildingMap.containsKey("floor")) {
            Object floorObj = buildingMap.get("floor");
            if (floorObj instanceof Number) {
                buildingDto.setFloor(((Number) floorObj).intValue());
            } else if (floorObj instanceof String) {
                try {
                    buildingDto.setFloor(Integer.parseInt((String) floorObj));
                } catch (NumberFormatException e) {
                    buildingDto.setFloor(null);
                }
            }
        }

        buildingDto.setUnit(mapToString(buildingMap.get("unit")));

        return buildingDto;
    }

    // 工具方法：類型轉換
    @Named("mapToString")
    default String mapToString(Object value) {
        return value != null ? value.toString() : null;
    }

    @Named("mapToLong")
    default Long mapToLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
```

### 實現核心

1. **逐層處理嵌套結構**：我們為每一層嵌套創建了專門的映射方法，如 `mapToAddress` 和 `mapToBuilding`。
2. **類型安全檢查**：每一層都進行了類型檢查和空值處理，確保映射穩健。
3. **遞歸處理**：上層方法調用下層方法，形成遞歸處理模式。
4. **通用轉換方法**：提供了通用的類型轉換方法，如 `mapToString` 和 `mapToLong`。

### 使用示例

以下是如何在控制器中使用這個映射器：

```java
@RestController
@RequestMapping("/api/nested-map")
public class NestedMapController {

    private final NestedMapMapper nestedMapMapper;

    @Autowired
    public NestedMapController(NestedMapMapper nestedMapMapper) {
        this.nestedMapMapper = nestedMapMapper;
    }

    @PostMapping("/convert")
    public ResponseEntity<CustomerDto> convertNestedMap(@RequestBody Map<String, Object> customerMap) {
        CustomerDto customerDto = nestedMapMapper.mapToCustomerDto(customerMap);
        return ResponseEntity.ok(customerDto);
    }
}
```

### 優勢

此方法有以下優勢：

1. **處理任意深度**：可以處理任意層級的嵌套結構。
2. **清晰的責任劃分**：每個方法只負責處理一層結構，邏輯清晰。
3. **靈活性**：可以根據需要為每一層添加特定的轉換邏輯。
4. **穩健性**：全面的類型檢查和空值處理確保映射過程的穩健性。
5. **可擴展性**：可以輕鬆擴展支持更多層級或更複雜的結構。

### 實際應用

這種多層嵌套映射在以下場景中特別有用：

1. **複雜 API 響應處理**：處理包含多層嵌套的 API 響應。
2. **層級化配置解析**：解析具有多層結構的配置文件。
3. **文檔數據轉換**：處理如 JSON 或 XML 等具有層級結構的文檔數據。
4. **數據整合**：整合來自多個來源的複雜結構數據。

### 性能考量

雖然遞歸處理很方便，但也需要注意以下性能方面的考量：

1. **循環引用**：確保數據中沒有循環引用，避免無限遞歸。
2. **深度控制**：對於非常深的結構，可以考慮添加深度限制。
3. **懶加載**：對於非常大的數據結構，考慮使用懶加載策略。

通過這種模式，我們可以有效處理現實世界中常見的複雜嵌套數據結構，而 MapStruct 的類型安全特性和編譯時代碼生成能確保高效的映射實現。
