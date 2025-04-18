package com.example.mapstructdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TruTakeFileTranrs implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 清單
     */
    @JsonProperty("Records")
    private List<TruTakeFileTranrsRecord> records;

    /**
     * 項目
     */
    @JsonProperty("Records")
    private List<TruTakeFileTranrsItem> items;

    @JsonProperty("ID")
    private String id;

}