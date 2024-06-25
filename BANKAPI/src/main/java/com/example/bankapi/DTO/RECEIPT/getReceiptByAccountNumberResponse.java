package com.example.bankapi.DTO.RECEIPT;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class getReceiptByAccountNumberResponse {
    private String accountNumber;
    private String amount;
    private LocalDateTime transactionDate;
    private String receiveAccountNumber;
    private String receiptType;
}
