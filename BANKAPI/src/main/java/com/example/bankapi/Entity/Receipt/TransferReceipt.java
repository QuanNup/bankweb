package com.example.bankapi.Entity.Receipt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class TransferReceipt extends Receipt{
    @Column(nullable = false,length = 16)
    String receiveAccountNumber;
    @Column(length = 255)
    String receiptDetail;
}
