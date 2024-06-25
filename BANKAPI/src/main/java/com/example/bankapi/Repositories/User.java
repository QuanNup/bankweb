package com.example.bankapi.Repositories;

import com.example.bankapi.Entity.Authentication.ROLE;
import com.example.bankapi.Entity.BankAccount.CheckingAccount;
import com.example.bankapi.Entity.BankAccount.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface User extends JpaRepository<com.example.bankapi.Entity.Authentication.User,Long> {
   com.example.bankapi.Entity.Authentication.User findByEmail(String email);
    boolean existsByRole(ROLE role);
    boolean existsByEmail(String email);
    List<SavingAccount> findUserByEmail(String email);
//    @Query("SELECT u.savingAccounts FROM User u WHERE u.email = :email")
//    List<SavingAccount> findSavingAccountsByEmail(@Param("email") String email);
//
//    @Query("SELECT u.checkingAccounts FROM User u WHERE u.email = :email")
//    List<CheckingAccount> findCheckingAccountsByEmail(@Param("email") String email);
}
