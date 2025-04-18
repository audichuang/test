package com.example.mapstructdemo.service;

import com.example.mapstructdemo.dto.EmptyTranrs;
import com.example.mapstructdemo.dto.QryyStatementA;
import com.example.mapstructdemo.dto.RequestTemplate;
import com.example.mapstructdemo.dto.ResponseTemplate;
import jakarta.validation.Valid;


/**
 * @author audi
 */
public interface AbcService {


    ResponseTemplate<EmptyTranrs> asdasdasd(@Valid RequestTemplate<QryyStatementA> customerDto);
}
