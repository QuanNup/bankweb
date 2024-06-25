package com.example.bankapi.Service.BankAccount.Strategy.impl;

import com.example.bankapi.Config.GlobalConfig.StaticVar;
import com.example.bankapi.Entity.BankAccount.Account;
import com.example.bankapi.Entity.BankAccount.SavingAccount;
import com.example.bankapi.Repositories.BankAccount.SavingAccountRepository;
import com.example.bankapi.Service.BankAccount.Strategy.AccountStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingAccountStrategy implements AccountStrategy {
    private final SavingAccountRepository repository;
    @Override
    public Account save(Account account) {
        return repository.save((SavingAccount) account);
    }

    public double getTotalDividend(String accountNumber){
        SavingAccount account = repository.findAccountByAccountNumber(accountNumber);
        if(account==null){
            throw new IllegalArgumentException(StaticVar.ACCOUNT_NOT_EXIST_EXCEPTION_MESSAGE);
        }
        LocalDateTime now = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(account.getCreateDate(),now);
        double totalBalance = account.getBalance()*Math.pow(1+StaticVar.INTEREST_RATE.doubleValue(), (double) days /365);
        double interest = totalBalance-account.getPrincipalAmount();
        return Math.round(interest* 100.0)/100.0;
    }

    public double getDividendToday(String accountNumber){
        SavingAccount account = repository.findAccountByAccountNumber(accountNumber);
        if(account==null){
            throw new IllegalArgumentException(StaticVar.ACCOUNT_NOT_EXIST_EXCEPTION_MESSAGE);
        }
        double totalBalance = account.getBalance()*(1+StaticVar.INTEREST_RATE.doubleValue()/365);
        double interest = totalBalance - account.getPrincipalAmount();
        return Math.round(interest*100.0)/100.0;
    }

    public List<SavingAccount> findAll(){
        return repository.findAll();
    }
}
