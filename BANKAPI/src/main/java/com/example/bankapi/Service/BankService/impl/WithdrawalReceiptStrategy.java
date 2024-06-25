package com.example.bankapi.Service.BankService.impl;

import com.example.bankapi.Entity.Receipt.Receipt;
import com.example.bankapi.Entity.Receipt.WithdrawalReceipt;
import com.example.bankapi.Repositories.Receipt.WithdrawalReceiptRepository;
import com.example.bankapi.Service.BankService.ReceiptStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawalReceiptStrategy implements ReceiptStrategy {
    private final WithdrawalReceiptRepository repository;
    @Override
    public Receipt save(Receipt receipt,Object...objects) {
        return  repository.save((WithdrawalReceipt) receipt);
    }
}
