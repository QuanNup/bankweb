package com.example.bankapi.DTO.RECEIPT;

import lombok.Data;

@Data
public class ReceiptCreateResponse {
    private String accountNumber;
    private double amount;
}
