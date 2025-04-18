package com.example.mapstructdemo.mapper;

import com.example.mapstructdemo.dto.ProductDto;
import com.example.mapstructdemo.entity.Product;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-13T07:31:02+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto productToProductDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto.ProductDtoBuilder productDto = ProductDto.builder();

        productDto.inStock( product.isAvailable() );
        productDto.createdDate( formatDateTime( product.getCreatedAt() ) );
        productDto.id( product.getId() );
        productDto.name( product.getName() );
        productDto.description( product.getDescription() );
        productDto.price( product.getPrice() );
        productDto.sku( product.getSku() );

        return productDto.build();
    }

    @Override
    public Product productDtoToProduct(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.available( productDto.isInStock() );
        product.id( productDto.getId() );
        product.name( productDto.getName() );
        product.description( productDto.getDescription() );
        product.price( productDto.getPrice() );
        product.sku( productDto.getSku() );

        product.updatedAt( java.time.LocalDateTime.now() );

        return product.build();
    }

    @Override
    public ProductDto mapToProductDto(Map<String, Object> productMap) {
        if ( productMap == null ) {
            return null;
        }

        ProductDto.ProductDtoBuilder productDto = ProductDto.builder();

        if ( productMap.containsKey( "product_id" ) ) {
            productDto.id( mapToLong( productMap.get( "product_id" ) ) );
        }
        if ( productMap.containsKey( "product_name" ) ) {
            productDto.name( mapToString( productMap.get( "product_name" ) ) );
        }
        if ( productMap.containsKey( "product_desc" ) ) {
            productDto.description( mapToString( productMap.get( "product_desc" ) ) );
        }
        if ( productMap.containsKey( "product_price" ) ) {
            productDto.price( mapToBigDecimal( productMap.get( "product_price" ) ) );
        }
        if ( productMap.containsKey( "product_sku" ) ) {
            productDto.sku( mapToString( productMap.get( "product_sku" ) ) );
        }
        if ( productMap.containsKey( "is_available" ) ) {
            productDto.inStock( mapToBoolean( productMap.get( "is_available" ) ) );
        }

        productDto.createdDate( java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) );

        return productDto.build();
    }

    @Override
    public void updateProductFromDto(ProductDto productDto, Product product) {
        if ( productDto == null ) {
            return;
        }

        product.setAvailable( productDto.isInStock() );
        if ( productDto.getId() != null ) {
            product.setId( productDto.getId() );
        }
        if ( productDto.getName() != null ) {
            product.setName( productDto.getName() );
        }
        if ( productDto.getDescription() != null ) {
            product.setDescription( productDto.getDescription() );
        }
        if ( productDto.getPrice() != null ) {
            product.setPrice( productDto.getPrice() );
        }
        if ( productDto.getSku() != null ) {
            product.setSku( productDto.getSku() );
        }
    }
}
