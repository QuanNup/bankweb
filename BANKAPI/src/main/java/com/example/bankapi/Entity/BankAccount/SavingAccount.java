package com.example.bankapi.Entity.BankAccount;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "account_id")
public class SavingAccount extends Account {
    private double totalDividend;
    private double dividendToday;
    private double principalAmount;
    private LocalDateTime lastInterestDate;
}
