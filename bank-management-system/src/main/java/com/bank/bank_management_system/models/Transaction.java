package com.bank.bank_management_system.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private double amount;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction() {
    }

    public Transaction(String type, double amount, String description, Account account) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.account = account;
    }

    public LocalDateTime getTransactionDate() {
        throw new UnsupportedOperationException("Unimplemented method 'getTransactionDate'");
    }
}
