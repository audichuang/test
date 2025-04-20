package com.example.mapstructdemo.controller;

import com.example.mapstructdemo.dto.*;
import com.example.mapstructdemo.mapper.NestedMapMapper;
import com.example.mapstructdemo.service.AbcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/nested-map")
@Tag(name = "嵌套Map映射", description = "演示如何將多層嵌套的Map結構映射到嵌套的DTO對象")
public class NestedMapController {


    @Autowired
    private AbcService abcSer;
    @Autowired
    private NestedMapMapper nestedMapMapper;

    /**
     * RET-B-TAKINGFILE 個法人信託取檔_取檔作業
     */
    @GetMapping("/example")
    @Operation(summary = "示例嵌套Map轉換", description = "創建一個測試用的嵌套Map並轉換為CustomerDto對象，展示多層嵌套結構的處理", responses = {@ApiResponse(responseCode = "200", description = "成功轉換", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDto.class)))})
    public ResponseEntity<CustomerDto> nestedMapExample() {
        // 創建一個主Map
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("id", 1001L);
        customerMap.put("name", "張三");
        customerMap.put("email", "zhang@example.com");
        customerMap.put("phone", "123-456-7890");

        // 創建嵌套的地址Map
        Map<String, Object> addressMap = new HashMap<>();
        addressMap.put("street", "和平路123號");
        addressMap.put("city", "台北市");
        addressMap.put("zipCode", "100");
        addressMap.put("country", "台灣");

        // 將地址Map放入客戶Map
        customerMap.put("address", addressMap);

        // 使用映射器轉換為DTO
        CustomerDto customerDto = nestedMapMapper.mapToCustomerDto(customerMap);

        return ResponseEntity.ok(customerDto);
    }

    /**
     * 處理客戶端提交的嵌套Map數據
     *
     * @param customerMap 包含嵌套地址信息的客戶數據Map
     * @return 轉換後的CustomerDto對象
     */

    /**
     * RET-B-TAKINGFILE 個法人信託取檔_取檔作業
     */
    @PostMapping("/convert")
    @Operation(summary = "轉換嵌套Map結構", description = "接收一個包含嵌套結構的JSON Map，並轉換為CustomerDto對象。可以處理多層嵌套結構，如客戶->地址->建築", responses = {@ApiResponse(responseCode = "200", description = "成功轉換", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDto.class)))})
    public ResponseEntity<CustomerDto> convertNestedMap(@RequestBody Map<String, Object> customerMap) {
        CustomerDto customerDto = nestedMapMapper.mapToCustomerDto(customerMap);
        return ResponseEntity.ok(customerDto);
    }


    /**
     * RET-B-TAKINGFILE 個法人信託取檔_取檔作業
     */
    @PostMapping
    public ResponseTemplate<QrywStatementB> convertCustomer(@Valid @RequestBody RequestTemplate<QryyStatementA> customerDto) {

    }


    /**
     *
     * @param customerDto
     * @return
     */
    @PostMapping
    public ResponseTemplate<EmptyTranrs> converasdasdtCustomer(@Valid @RequestBody RequestTemplate<QryyStatementA> customerDto) {
        return abcSer.asdasdasd(customerDto);
    }

//    /**
//     * RET-B-TAKINGFILE 個法人信託取檔_取檔作業
//     */
//    @PostMapping(path = "/takeFile", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseTemplate<TruTakeFileTranrs> takeFile(@RequestBody RequestTemplate<TruTakeFileTranrq> request) throws CustomException {
//    }
}