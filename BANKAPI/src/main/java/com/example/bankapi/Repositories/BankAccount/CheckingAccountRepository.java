package com.example.bankapi.Repositories.BankAccount;

import com.example.bankapi.Entity.BankAccount.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount,Long>,AbstractAccountRepository<CheckingAccount>{
}
