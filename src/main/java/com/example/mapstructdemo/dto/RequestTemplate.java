package com.example.mapstructdemo.dto;


import lombok.Data;

/**
 * @author : Audi
 * @date : 2025-03-27 19:45
 **/
@Data
public class RequestTemplate<T> {
    /**
     * 請求頭信息
     */
    private MwHeader header;

    /**
     * 請求體內容
     */
    private T tranrq;
}
