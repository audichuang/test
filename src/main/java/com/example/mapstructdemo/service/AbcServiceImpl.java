package com.example.mapstructdemo.service;

import com.example.mapstructdemo.dto.EmptyTranrs;
import com.example.mapstructdemo.dto.QryyStatementA;
import com.example.mapstructdemo.dto.RequestTemplate;
import com.example.mapstructdemo.dto.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author audi
 */
@Service
@RequiredArgsConstructor
public class AbcServiceImpl implements AbcService {


    @Autowired
    private AaaaService aaaaService;


    @Override
    public ResponseTemplate<EmptyTranrs> asdasdasd(RequestTemplate<QryyStatementA> customerDto) {
        return null;
    }
}
