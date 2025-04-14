package com.bank.bank_management_system.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BUSINESS")
public class BusinessAccount extends Account {
    public BusinessAccount() {
    }

    public BusinessAccount(User user, double initialBalance) {
        this.setUser(user);
        this.setBalance(initialBalance);
        this.setAccountNumber(User.generateAccountNumber());
    }

    @Override
    public String getAccountType() {
        return "BUSINESS";
    }
}