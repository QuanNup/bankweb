package com.example.bankapi.Controller.ReceiptController;

import com.example.bankapi.DTO.RECEIPT.ReceiptCreateRequest;
import com.example.bankapi.DTO.RECEIPT.ReceiptCreateResponse;
import com.example.bankapi.DTO.RECEIPT.getReceiptByAccountNumberRequest;
import com.example.bankapi.DTO.RECEIPT.getReceiptByAccountNumberResponse;
import com.example.bankapi.Service.BankService.ReceiptService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
@Tag(name="Receipt Controller")//gán tên cho swagger
@SecurityRequirement(name="bearerAuth")
public class ReceiptController {
    private final ReceiptService receiptService;

    @PostMapping(value="/create",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiptCreateResponse> createReceipt(@RequestBody ReceiptCreateRequest request){
        return ResponseEntity.ok(receiptService.create(request));
    }
    @PostMapping(value="/get-receipt-by-accountNumber")
    public List<getReceiptByAccountNumberResponse> Receipt(@RequestBody getReceiptByAccountNumberRequest request){
        return receiptService.getReceiptByAccountNumber(request);
    }
}
