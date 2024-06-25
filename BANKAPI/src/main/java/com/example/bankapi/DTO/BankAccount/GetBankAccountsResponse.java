package com.example.bankapi.DTO.BankAccount;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetBankAccountsResponse {
    private final List<GetBankAccountResponse> accounts = new ArrayList<>();
}
