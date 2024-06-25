package com.example.bankapi.DTO.BankAccount;

import lombok.Data;

import java.util.Date;

@Data
public class AccountRegistryResponse {
    private  String accountNumber;
    private Date createDate;
    private String accountType;
}
