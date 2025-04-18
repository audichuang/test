package com.example.mapstructdemo.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * @author : Audi
 * @date : 2025-03-27 19:45
 **/
@Data
public class MwHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msgid;
}
