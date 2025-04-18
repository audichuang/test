package com.example.mapstructdemo.mapper;

import com.example.mapstructdemo.dto.ProductDto;
import com.example.mapstructdemo.entity.Product;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    // 自定義映射方法：LocalDateTime -> String
    default String localDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // 基本映射：Entity -> DTO
    @Mapping(source = "available", target = "inStock")
    @Mapping(source = "createdAt", target = "createdDate", qualifiedByName = "formatDateTime")
    ProductDto productToProductDto(Product product);

    // 基本映射：DTO -> Entity
    @Mapping(source = "inStock", target = "available")
    @Mapping(target = "createdAt", ignore = true) // 忽略自動映射
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())") // 使用表達式賦值
    Product productDtoToProduct(ProductDto productDto);

    // Map -> DTO 映射
    @Mapping(source = "product_id", target = "id", qualifiedByName = "mapToLong")
    @Mapping(source = "product_name", target = "name", qualifiedByName = "mapToString")
    @Mapping(source = "product_desc", target = "description", qualifiedByName = "mapToString")
    @Mapping(source = "product_price", target = "price", qualifiedByName = "mapToBigDecimal")
    @Mapping(source = "product_sku", target = "sku", qualifiedByName = "mapToString")
    @Mapping(source = "is_available", target = "inStock", qualifiedByName = "mapToBoolean")
    @Mapping(target = "createdDate", expression = "java(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\")))")
    ProductDto mapToProductDto(Map<String, Object> productMap);

    // 命名的映射方法
    @Named("formatDateTime")
    default String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // 更新現有實體對象
    @Mapping(source = "inStock", target = "available")
    void updateProductFromDto(ProductDto productDto, @MappingTarget Product product);

    // 從 Object 到各種類型的轉換方法
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
            return Boolean.parseBoolean((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        return false;
    }
}