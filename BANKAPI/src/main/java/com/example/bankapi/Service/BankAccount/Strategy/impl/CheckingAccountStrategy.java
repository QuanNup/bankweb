package com.example.bankapi.Service.BankAccount.Strategy.impl;

import com.example.bankapi.Entity.BankAccount.Account;
import com.example.bankapi.Entity.BankAccount.CheckingAccount;
import com.example.bankapi.Repositories.BankAccount.CheckingAccountRepository;
import com.example.bankapi.Service.BankAccount.Strategy.AccountStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckingAccountStrategy implements AccountStrategy {
    private final CheckingAccountRepository repository;

    @Override
    public Account save(Account account) {
        return repository.save((CheckingAccount) account);
    }

    public List<CheckingAccount> findAll(){
        return repository.findAll();
    }

}
