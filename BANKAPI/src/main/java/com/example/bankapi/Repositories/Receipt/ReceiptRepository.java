package com.example.bankapi.Repositories.Receipt;

import com.example.bankapi.Entity.Receipt.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt,Long> {
}
