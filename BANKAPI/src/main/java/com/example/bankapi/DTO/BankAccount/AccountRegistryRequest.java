package com.example.bankapi.DTO.BankAccount;

import lombok.Data;

@Data
public class AccountRegistryRequest {
    private String userEmail;
    private String accountType;
    private String accountNumber;
}
