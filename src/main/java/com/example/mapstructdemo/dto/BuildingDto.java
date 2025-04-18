package com.example.mapstructdemo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BuildingDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer floor;
    private String unit;
}