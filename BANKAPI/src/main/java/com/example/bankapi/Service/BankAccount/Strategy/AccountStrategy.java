package com.example.bankapi.Service.BankAccount.Strategy;

import com.example.bankapi.Entity.BankAccount.Account;

import java.util.List;

public interface AccountStrategy {
    Account save(Account account);
}
