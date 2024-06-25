package com.example.bankapi.Repositories.BankAccount;

import com.example.bankapi.Entity.BankAccount.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long>,AbstractAccountRepository<Account>{

}
