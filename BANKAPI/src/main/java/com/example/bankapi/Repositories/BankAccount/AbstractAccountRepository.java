package com.example.bankapi.Repositories.BankAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractAccountRepository<T> extends JpaRepository<T,Long> {
    T findAccountByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
}
