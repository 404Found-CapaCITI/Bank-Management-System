package com.bank.bank_management_system.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SAVINGS")
public class SavingsAccount extends Account {
    public SavingsAccount() {}

    public SavingsAccount(User user, double initialBalance) {
        this.setUser(user);
        this.setBalance(initialBalance);
        this.setAccountNumber(User.generateAccountNumber());
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }
}