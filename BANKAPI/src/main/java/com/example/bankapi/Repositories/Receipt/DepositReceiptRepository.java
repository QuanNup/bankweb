package com.example.bankapi.Repositories.Receipt;

import com.example.bankapi.Entity.Receipt.DepositReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositReceiptRepository extends JpaRepository<DepositReceipt,Long> {
}
