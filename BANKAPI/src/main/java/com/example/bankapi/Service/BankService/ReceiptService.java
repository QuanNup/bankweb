package com.example.bankapi.Service.BankService;

import com.example.bankapi.Config.GlobalConfig.StaticVar;
import com.example.bankapi.DTO.RECEIPT.ReceiptCreateRequest;
import com.example.bankapi.DTO.RECEIPT.ReceiptCreateResponse;
import com.example.bankapi.DTO.RECEIPT.getReceiptByAccountNumberRequest;
import com.example.bankapi.DTO.RECEIPT.getReceiptByAccountNumberResponse;
import com.example.bankapi.Entity.BankAccount.Account;
import com.example.bankapi.Entity.Receipt.DepositReceipt;
import com.example.bankapi.Entity.Receipt.Receipt;
import com.example.bankapi.Entity.Receipt.TransferReceipt;
import com.example.bankapi.Entity.Receipt.WithdrawalReceipt;
import com.example.bankapi.Service.BankAccount.AccountService;
import com.example.bankapi.Service.BankService.impl.DepositReceiptStrategy;
import com.example.bankapi.Service.BankService.impl.TransferReceiptStrategy;
import com.example.bankapi.Service.BankService.impl.WithdrawalReceiptStrategy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReceiptService {
    private final Map<Class<? extends Receipt>, ReceiptStrategy> strategies = new HashMap<>();
    private final ModelMapper modelMapper;
    @PersistenceContext
    private final EntityManager manager;
    private final AccountService accountService ;
    public ReceiptService(DepositReceiptStrategy depositReceiptStrategy,
                          TransferReceiptStrategy transferReceiptStrategy,
                          WithdrawalReceiptStrategy withdrawalReceiptStrategy,
                          ModelMapper modelMapper,
                          AccountService accountService,
                          EntityManager manager){
        this.strategies.put(DepositReceipt.class,depositReceiptStrategy);
        this.strategies.put(TransferReceipt.class,transferReceiptStrategy);
        this.strategies.put(WithdrawalReceipt.class,withdrawalReceiptStrategy);
        this.modelMapper = modelMapper;
        this.accountService= accountService;
        this.manager = manager;
    }
    @Transactional
    public ReceiptCreateResponse create(ReceiptCreateRequest request){
        Receipt receipt = CreateInforReceipt(request);
        ReceiptStrategy strategy = strategies.get(receipt.getClass());
        if(accountService.handleBalanceByReceipt(receipt,request.getReceiveAccountNumber())){
            Receipt receipt1 = strategy.save(receipt,request);
            return modelMapper.map(receipt1,ReceiptCreateResponse.class);
        }
//        return modelMapper.map(strategy.save(receipt,request),ReceiptCreateResponse.class);
        return null;
    }
    public List<getReceiptByAccountNumberResponse> getReceiptByAccountNumber(getReceiptByAccountNumberRequest request){
        TypedQuery<Receipt> query = manager.createQuery("SELECT r FROM Receipt r WHERE r.account.accountNumber = :accountNumber", Receipt.class);
        query.setParameter("accountNumber", request.getAccountNumber());
        List<Receipt> receipts = query.getResultList();
        List<getReceiptByAccountNumberResponse> responses = new ArrayList<>();
        for(Receipt receipt: receipts){
            responses.add(modelMapper.map(receipt,getReceiptByAccountNumberResponse.class));
        }
        return responses;
    }
    private Receipt CreateInforReceipt(ReceiptCreateRequest request) {
        var senderAccount = accountService.findByAccountNumber(request.getAccountNumber());

        var receipt = modelMapper.map(request,Receipt.class);
        receipt.setTransactionDate(LocalDateTime.now());
        receipt.setAccount(senderAccount);
        switch (receipt.getReceiptType().toUpperCase()) {
            case StaticVar.DEPOSIT_RECEIPT -> {
                return modelMapper.map(receipt,DepositReceipt.class);
            }
            case StaticVar.WITHDRAWAL_RECEIPT -> {
                return modelMapper.map(receipt,WithdrawalReceipt.class);
            }
            case StaticVar.TRANSFER_RECEIPT -> {
                return modelMapper.map(receipt,TransferReceipt.class);
            }
            default -> {
                throw new IllegalArgumentException("no strategy found for receipt type "+ request.getReceiptType());
            }
        }

    }
}
