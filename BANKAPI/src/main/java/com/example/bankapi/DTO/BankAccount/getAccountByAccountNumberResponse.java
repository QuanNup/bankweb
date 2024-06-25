package com.example.bankapi.DTO.BankAccount;

import lombok.Data;

@Data
public class getAccountByAccountNumberResponse {
    String fullName;
    String accountNumber;
    String accountType;
}
