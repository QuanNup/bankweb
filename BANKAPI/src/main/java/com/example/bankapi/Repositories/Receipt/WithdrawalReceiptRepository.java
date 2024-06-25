package com.example.bankapi.Repositories.Receipt;

import com.example.bankapi.Entity.Receipt.WithdrawalReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalReceiptRepository extends JpaRepository<WithdrawalReceipt,Long> {
}
