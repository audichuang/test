package com.example.mapstructdemo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String street;
    private String city;
    private String zipCode;
    private String country;
    private BuildingDto building;
}