package com.example.bankapi.Service.BankService.impl;

import com.example.bankapi.Entity.Receipt.Receipt;
import com.example.bankapi.Entity.Receipt.TransferReceipt;
import com.example.bankapi.Entity.Receipt.WithdrawalReceipt;
import com.example.bankapi.Repositories.Receipt.TransferReceiptRepository;
import com.example.bankapi.Repositories.Receipt.WithdrawalReceiptRepository;
import com.example.bankapi.Service.BankService.ReceiptStrategy;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferReceiptStrategy implements ReceiptStrategy {
    private final TransferReceiptRepository repository;
    private final ModelMapper modelMapper;
    @Override
    public Receipt save(Receipt receipt,Object...objects) {
        var specificField = modelMapper.map(objects[0],TransferReceipt.class);
        var transferReceipt = (TransferReceipt) receipt;
        transferReceipt.setReceiveAccountNumber(specificField.getReceiveAccountNumber());
        if(specificField.getReceiptDetail()==null){
            transferReceipt.setReceiptDetail("Transfer "+transferReceipt.getAmount() +" from "+transferReceipt.getAccount().getAccountNumber()+"To "+transferReceipt.getReceiveAccountNumber());
        }else{
            transferReceipt.setReceiptDetail(specificField.getReceiptDetail());
        }
        return  repository.save((TransferReceipt) receipt);
    }
}
