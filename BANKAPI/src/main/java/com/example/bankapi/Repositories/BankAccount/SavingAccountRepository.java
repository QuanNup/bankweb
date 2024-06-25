package com.example.bankapi.Repositories.BankAccount;

import com.example.bankapi.Entity.BankAccount.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingAccountRepository extends JpaRepository<SavingAccount,Long>,AbstractAccountRepository<SavingAccount>{
}
