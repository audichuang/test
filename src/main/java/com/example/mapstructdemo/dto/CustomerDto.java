package com.example.mapstructdemo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String email;
    private String phone;
    private AddressDto address; // 嵌套的AddressDto對象
}