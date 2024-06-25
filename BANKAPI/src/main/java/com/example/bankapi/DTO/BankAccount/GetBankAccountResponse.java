package com.example.bankapi.DTO.BankAccount;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class GetBankAccountResponse {
    private String accountNumber;
    private String accountType;
    private double balance;
    private LocalDateTime createDate;
}
