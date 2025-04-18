package com.example.mapstructdemo.mapper;

import com.example.mapstructdemo.dto.ProductDto;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Map結構到DTO的映射示例
 * 用於處理來自外部API、JSON或動態數據源的Map數據
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapStructExampleMapper {

    /**
     * 將複雜的Map結構轉換為ProductDto
     * 支持嵌套Map、列表和不同類型的數據
     *
     * 主要使用自定義的getNestedValue方法來訪問嵌套的Map結構
     * 例如: product.id, product.name等
     */
    @Mapping(target = "id", expression = "java(getNestedValue(product, \"product.id\", Long.class))")
    @Mapping(target = "name", expression = "java(getNestedValue(product, \"product.name\", String.class))")
    @Mapping(target = "description", expression = "java(getNestedValue(product, \"product.description\", String.class))")
    @Mapping(target = "price", expression = "java(getNestedBigDecimal(product, \"product.price\"))")
    @Mapping(target = "sku", expression = "java(getNestedValue(product, \"product.sku\", String.class))")
    @Mapping(target = "inStock", expression = "java(getNestedBoolean(product, \"product.available\"))")
    @Mapping(target = "createdDate", expression = "java(formatDate(product.get(\"createTime\")))")
    ProductDto complexMapToProductDto(Map<String, Object> product);

    /**
     * 將扁平的Map結構(所有字段在一層)轉換為ProductDto
     */
    @Mapping(source = "id", target = "id", qualifiedByName = "mapToLong")
    @Mapping(source = "name", target = "name", qualifiedByName = "mapToString")
    @Mapping(source = "desc", target = "description", qualifiedByName = "mapToString")
    @Mapping(source = "price", target = "price", qualifiedByName = "mapToBigDecimal")
    @Mapping(source = "code", target = "sku", qualifiedByName = "mapToString")
    @Mapping(source = "in_stock", target = "inStock", qualifiedByName = "mapToBoolean")
    @Mapping(source = "created_at", target = "createdDate", qualifiedByName = "mapDateString")
    ProductDto flatMapToProductDto(Map<String, Object> product);

    /**
     * 處理Map<String, List<Map<String, Object>>>格式的數據，提取產品列表
     */
    default List<ProductDto> processProductsResponse(Map<String, Object> response) {
        if (response == null || !response.containsKey("products")) {
            return Collections.emptyList();
        }

        try {
            Object productsObj = response.get("products");
            if (!(productsObj instanceof List)) {
                return Collections.emptyList();
            }

            List<?> productsList = (List<?>) productsObj;
            return productsList.stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> (Map<String, Object>) item)
                    .map(this::flatMapToProductDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 從嵌套的Map中獲取值
     * 路徑格式為: "key1.key2.key3"
     *
     * 該方法解決了MapStruct無法直接處理嵌套Map的限制
     * 通過遞歸解析路徑訪問多層嵌套結構
     *
     * 優點:
     * 1. 可以處理任意層級的嵌套結構
     * 2. 支持類型安全轉換
     * 3. 處理空值和路徑不存在的情況
     */
    default <T> T getNestedValue(Map<String, Object> map, String path, Class<T> type) {
        if (map == null || path == null || path.isEmpty()) {
            return null;
        }

        String[] keys = path.split("\\.");
        Object current = map;

        for (String key : keys) {
            if (current instanceof Map) {
                Map<?, ?> currentMap = (Map<?, ?>) current;
                if (currentMap.containsKey(key)) {
                    current = currentMap.get(key);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        if (current == null) {
            return null;
        }

        if (type.isInstance(current)) {
            return type.cast(current);
        } else if (type == String.class) {
            return (T) mapToString(current);
        } else if (type == Long.class) {
            return (T) mapToLong(current);
        }

        return null;
    }

    /**
     * 從嵌套的Map中獲取BigDecimal值
     *
     * 提供專門處理BigDecimal類型的嵌套Map訪問方法
     * 先獲取原始值然後用mapToBigDecimal進行類型轉換
     */
    default BigDecimal getNestedBigDecimal(Map<String, Object> map, String path) {
        Object value = getNestedValue(map, path, Object.class);
        return mapToBigDecimal(value);
    }

    /**
     * 從嵌套的Map中獲取Boolean值
     *
     * 提供專門處理Boolean類型的嵌套Map訪問方法
     * 支持直接獲取Boolean值或進行類型轉換
     */
    default boolean getNestedBoolean(Map<String, Object> map, String path) {
        Boolean value = getNestedValue(map, path, Boolean.class);
        if (value != null) {
            return value;
        }
        Object rawValue = getNestedValue(map, path, Object.class);
        return mapToBoolean(rawValue);
    }

    /**
     * 將任意日期格式的物件轉換為標準格式字串
     */
    default String formatDate(Object dateObj) {
        if (dateObj == null) {
            return null;
        }

        try {
            if (dateObj instanceof String) {
                // 處理ISO日期字符串
                LocalDateTime dateTime = LocalDateTime.parse((String) dateObj,
                        DateTimeFormatter.ISO_DATE_TIME);
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else if (dateObj instanceof Number) {
                // 處理時間戳
                LocalDateTime dateTime = LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochMilli(((Number) dateObj).longValue()),
                        java.time.ZoneId.systemDefault());
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else if (dateObj instanceof Date) {
                // 處理Java日期
                LocalDateTime dateTime = LocalDateTime.ofInstant(
                        ((Date) dateObj).toInstant(), TimeZone.getDefault().toZoneId());
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            // 解析失敗返回null
        }

        return null;
    }

    @Named("mapDateString")
    default String mapDateString(Object value) {
        return formatDate(value);
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

    @Named("mapToString")
    default String mapToString(Object value) {
        return value != null ? value.toString() : null;
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
            return new BigDecimal(((Number) value).doubleValue());
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
            String strValue = (String) value;
            return "true".equalsIgnoreCase(strValue) ||
                    "yes".equalsIgnoreCase(strValue) ||
                    "1".equals(strValue) ||
                    "on".equalsIgnoreCase(strValue);
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        return false;
    }
}