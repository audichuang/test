package com.example.mapstructdemo.mapper;

import com.example.mapstructdemo.dto.CustomerDto;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-13T07:31:03+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class NestedMapMapperImpl implements NestedMapMapper {

    @Override
    public CustomerDto mapToCustomerDto(Map<String, Object> customerMap) {
        if ( customerMap == null ) {
            return null;
        }

        CustomerDto customerDto = new CustomerDto();

        if ( customerMap.containsKey( "id" ) ) {
            customerDto.setId( mapToLong( customerMap.get( "id" ) ) );
        }
        if ( customerMap.containsKey( "name" ) ) {
            customerDto.setName( mapToString( customerMap.get( "name" ) ) );
        }
        if ( customerMap.containsKey( "email" ) ) {
            customerDto.setEmail( mapToString( customerMap.get( "email" ) ) );
        }
        if ( customerMap.containsKey( "phone" ) ) {
            customerDto.setPhone( mapToString( customerMap.get( "phone" ) ) );
        }

        customerDto.setAddress( mapToAddress(customerMap) );

        return customerDto;
    }
}
