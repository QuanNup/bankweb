package com.example.bankapi.DTO.RECEIPT;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReceiptCreateRequest {
    private String accountNumber;
    private double amount;
    private String receiptType;
    private String receiveAccountNumber;
}
