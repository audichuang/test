package com.example.mapstructdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TruTakeFileTranrq implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    @JsonProperty("BillDate")
    private String billDate;

    @JsonProperty("BillType")
    private String billType;

    @JsonProperty("FileName")
    private String fileName;

    @JsonProperty("LoadEmpId")
    private String loadEmpId;

    @JsonProperty("LoadDate")
    private String loadDate;
    
    @JsonProperty("UserId")
    private String userId;

}