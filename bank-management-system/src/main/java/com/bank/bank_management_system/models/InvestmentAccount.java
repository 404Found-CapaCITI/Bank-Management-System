package com.bank.bank_management_system.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("INVESTMENT")
public class InvestmentAccount extends Account {
    public InvestmentAccount() {}

    public InvestmentAccount(User user, double initialBalance) {
        this.setUser(user);
        this.setBalance(initialBalance);
        this.setAccountNumber(User.generateAccountNumber());
    }

    @Override
    public String getAccountType() {
        return "INVESTMENT";
    }
}