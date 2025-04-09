package com.bank.bank_management_system.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private String type;
    private String description;
    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @PrePersist
    public void prePersist() {
        this.transactionDate = LocalDateTime.now();
    }
}