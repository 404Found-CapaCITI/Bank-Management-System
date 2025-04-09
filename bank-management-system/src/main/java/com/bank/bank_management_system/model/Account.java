package com.bank.bank_management_system.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.bank.bank_management_system.exception.InsufficientBalanceException;

@Data
@Entity
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String accountNumber;
    private double balance;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
    
    public abstract String getAccountType();
    
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance += amount;
    }
    
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }
        this.balance -= amount;
    }
}

@Entity
@DiscriminatorValue("SAVINGS")
class SavingsAccount extends Account {
    @Override
    public String getAccountType() {
        return "SAVINGS";
    }
}

@Entity
@DiscriminatorValue("CHECKING")
class CheckingAccount extends Account {
    @Override
    public String getAccountType() {
        return "CHECKING";
    }
}

@Entity
@DiscriminatorValue("BUSINESS")
class BusinessAccount extends Account {
    @Override
    public String getAccountType() {
        return "BUSINESS";
    }
}

@Entity
@DiscriminatorValue("INVESTMENT")
class InvestmentAccount extends Account {
    @Override
    public String getAccountType() {
        return "INVESTMENT";
    }
}
