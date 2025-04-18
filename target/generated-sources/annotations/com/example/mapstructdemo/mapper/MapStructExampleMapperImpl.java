package com.example.mapstructdemo.mapper;

import com.example.mapstructdemo.dto.ProductDto;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-13T07:31:03+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class MapStructExampleMapperImpl implements MapStructExampleMapper {

    @Override
    public ProductDto complexMapToProductDto(Map<String, Object> product) {
        if ( product == null ) {
            return null;
        }

        ProductDto.ProductDtoBuilder productDto = ProductDto.builder();

        productDto.id( getNestedValue(product, "product.id", Long.class) );
        productDto.name( getNestedValue(product, "product.name", String.class) );
        productDto.description( getNestedValue(product, "product.description", String.class) );
        productDto.price( getNestedBigDecimal(product, "product.price") );
        productDto.sku( getNestedValue(product, "product.sku", String.class) );
        productDto.inStock( getNestedBoolean(product, "product.available") );
        productDto.createdDate( formatDate(product.get("createTime")) );

        return productDto.build();
    }

    @Override
    public ProductDto flatMapToProductDto(Map<String, Object> product) {
        if ( product == null ) {
            return null;
        }

        ProductDto.ProductDtoBuilder productDto = ProductDto.builder();

        if ( product.containsKey( "id" ) ) {
            productDto.id( mapToLong( product.get( "id" ) ) );
        }
        if ( product.containsKey( "name" ) ) {
            productDto.name( mapToString( product.get( "name" ) ) );
        }
        if ( product.containsKey( "desc" ) ) {
            productDto.description( mapToString( product.get( "desc" ) ) );
        }
        if ( product.containsKey( "price" ) ) {
            productDto.price( mapToBigDecimal( product.get( "price" ) ) );
        }
        if ( product.containsKey( "code" ) ) {
            productDto.sku( mapToString( product.get( "code" ) ) );
        }
        if ( product.containsKey( "in_stock" ) ) {
            productDto.inStock( mapToBoolean( product.get( "in_stock" ) ) );
        }
        if ( product.containsKey( "created_at" ) ) {
            productDto.createdDate( mapDateString( product.get( "created_at" ) ) );
        }

        return productDto.build();
    }
}
