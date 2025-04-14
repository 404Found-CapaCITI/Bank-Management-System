package com.bank.bank_management_system.models;

import java.util.ArrayList;
import java.util.List;

import com.bank.bank_management_system.exception.InsufficientBalanceException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "transactions" })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    public abstract String getAccountType();

    public void deposit(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Deposit amount must be positive");
        this.balance += amount;
        this.transactions.add(new Transaction("DEPOSIT", amount, "Deposit to account", this));
    }

    public void withdraw(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        if (amount > balance)
            throw new InsufficientBalanceException("Insufficient balance");
        this.balance -= amount;
        this.transactions.add(new Transaction("WITHDRAWAL", amount, "Withdrawal from account", this));
    }

    public void transfer(Account recipient, double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Transfer amount must be positive");
        if (amount > balance)
            throw new InsufficientBalanceException("Insufficient balance");
        this.balance -= amount;
        recipient.balance += amount;
        this.transactions
                .add(new Transaction("TRANSFER_OUT", amount, "Transfer to " + recipient.getAccountNumber(), this));
        recipient.transactions
                .add(new Transaction("TRANSFER_IN", amount, "Transfer from " + this.getAccountNumber(), recipient));
    }
}