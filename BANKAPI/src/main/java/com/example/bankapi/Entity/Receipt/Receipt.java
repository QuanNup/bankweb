package com.example.bankapi.Entity.Receipt;

import com.example.bankapi.Entity.BankAccount.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    @Column(nullable = false)
    private String accountNumber;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private LocalDateTime transactionDate;
    @Column(nullable = false)
    private String receiptType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="account_id",referencedColumnName = "id")
    private Account account;
}
