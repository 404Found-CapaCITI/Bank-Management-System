package com.bank.bank_management_system.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CHECKING")
public class CheckingAccount extends Account {
    public CheckingAccount() {}

    public CheckingAccount(User user, double initialBalance) {
        this.setUser(user);
        this.setBalance(initialBalance);
        this.setAccountNumber(User.generateAccountNumber());
    }

    @Override
    public String getAccountType() {
        return "CHECKING";
    }
}