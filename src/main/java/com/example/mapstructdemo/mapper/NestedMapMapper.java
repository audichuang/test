package com.example.mapstructdemo.mapper;

import com.example.mapstructdemo.dto.AddressDto;
import com.example.mapstructdemo.dto.BuildingDto;
import com.example.mapstructdemo.dto.CustomerDto;
import org.mapstruct.*;

import java.util.Map;

/**
 * 嵌套Map結構到嵌套對象的映射示例
 * 處理Map中包含另一個Map的情況，映射到類中包含另一個類的情況
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NestedMapMapper {

    /**
     * 將嵌套Map結構映射到CustomerDto，其中CustomerDto包含AddressDto對象
     * 
     * @param customerMap 包含客戶信息和地址信息的嵌套Map
     * @return 映射後的CustomerDto對象，包含AddressDto
     */
    @Mapping(target = "id", source = "id", qualifiedByName = "mapToLong")
    @Mapping(target = "name", source = "name", qualifiedByName = "mapToString")
    @Mapping(target = "email", source = "email", qualifiedByName = "mapToString")
    @Mapping(target = "phone", source = "phone", qualifiedByName = "mapToString")
    @Mapping(target = "address", expression = "java(mapToAddress(customerMap))")
    CustomerDto mapToCustomerDto(Map<String, Object> customerMap);

    /**
     * 從Map中提取地址信息並映射到AddressDto
     * 
     * @param customerMap 包含地址信息的Map
     * @return 映射後的AddressDto對象
     */
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

        if (addressMap.containsKey("street")) {
            addressDto.setStreet(mapToString(addressMap.get("street")));
        }

        if (addressMap.containsKey("city")) {
            addressDto.setCity(mapToString(addressMap.get("city")));
        }

        if (addressMap.containsKey("zipCode")) {
            addressDto.setZipCode(mapToString(addressMap.get("zipCode")));
        }

        if (addressMap.containsKey("country")) {
            addressDto.setCountry(mapToString(addressMap.get("country")));
        }

        // 處理嵌套的Building對象
        if (addressMap.containsKey("building")) {
            addressDto.setBuilding(mapToBuilding(addressMap.get("building")));
        }

        return addressDto;
    }

    /**
     * 從Map中提取建築信息並映射到BuildingDto
     * 
     * @param buildingObj 建築信息對象
     * @return 映射後的BuildingDto對象
     */
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

        if (buildingMap.containsKey("unit")) {
            buildingDto.setUnit(mapToString(buildingMap.get("unit")));
        }

        return buildingDto;
    }

    /**
     * 將Object轉換為String
     */
    @Named("mapToString")
    default String mapToString(Object value) {
        return value != null ? value.toString() : null;
    }

    /**
     * 將Object轉換為Long
     */
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