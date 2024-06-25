package com.example.bankapi.Service.BankService.impl;

import com.example.bankapi.Entity.Receipt.DepositReceipt;
import com.example.bankapi.Entity.Receipt.Receipt;
import com.example.bankapi.Repositories.Receipt.DepositReceiptRepository;
import com.example.bankapi.Service.BankService.ReceiptStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DepositReceiptStrategy implements ReceiptStrategy {
    private final DepositReceiptRepository repository;

    @Override
    public Receipt save(Receipt receipt, Object... objects) {
        return repository.save((DepositReceipt) receipt);
    }
}
