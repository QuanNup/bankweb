package com.example.bankapi.Service.Task;

import com.example.bankapi.Entity.BankAccount.SavingAccount;
import com.example.bankapi.Repositories.BankAccount.SavingAccountRepository;
import com.example.bankapi.Service.BankAccount.Strategy.impl.SavingAccountStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
@Component
@RequiredArgsConstructor
public class InterestCalculationTask  {
    private final SavingAccountStrategy savingAccountService;
    private final SavingAccountRepository repository;
    @Scheduled(cron = "0 0 0 * * ?")
    public void calculateInterest() {
        List<SavingAccount> accounts = savingAccountService.findAll();
        for(SavingAccount account: accounts){
            double totalDividend= savingAccountService.getTotalDividend(account.getAccountNumber());
            account.setDividendToday(savingAccountService.getDividendToday(account.getAccountNumber()));
            account.setTotalDividend(totalDividend);
            account.setBalance(totalDividend+account.getPrincipalAmount());
            account.setLastInterestDate(LocalDateTime.now());
            repository.save(account);
        }
    }
}
