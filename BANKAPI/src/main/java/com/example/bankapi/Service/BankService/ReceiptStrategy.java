package com.example.bankapi.Service.BankService;

import com.example.bankapi.Entity.Receipt.Receipt;

public interface ReceiptStrategy {
    Receipt save(Receipt receipt,Object...objects);

}
