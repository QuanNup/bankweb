package com.example.bankapi.Repositories.Receipt;

import com.example.bankapi.Entity.Receipt.TransferReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferReceiptRepository extends JpaRepository<TransferReceipt,Long> {
}
