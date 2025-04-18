package com.example.mapstructdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class QryyStatementA implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @JsonProperty("Id")
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /** 信箱 */
    @JsonProperty("Email")
    private String email;
    private String phone;
    private QryStatementTranrqAddress address; // 嵌套的AddressDto對象
}