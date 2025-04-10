package com.bank.bank_management_system.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CURRENT")
public class CurrentAccount extends Account {
    public CurrentAccount() {
    }

    public CurrentAccount(User user, double initialBalance) {
        this.setUser(user);
        this.setBalance(initialBalance);
        this.setAccountNumber(user.getAccountNumber());
    }

    @Override
    public String getAccountType() {
        return "CURRENT";
    }
}