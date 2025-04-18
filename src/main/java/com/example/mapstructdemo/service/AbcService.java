package com.example.mapstructdemo.service;

import com.example.mapstructdemo.dto.EmptyTranrs;
import com.example.mapstructdemo.dto.QryyStatementA;
import com.example.mapstructdemo.dto.RequestTemplate;
import com.example.mapstructdemo.dto.ResponseTemplate;
import jakarta.validation.Valid;


/**
 * RET-B-TAKINGFILE 個法人信託取檔_取檔作業
 *
 * @author audi
 */
public interface AbcService {


    ResponseTemplate<EmptyTranrs> asdasdasd(@Valid RequestTemplate<QryyStatementA> customerDto);
}
