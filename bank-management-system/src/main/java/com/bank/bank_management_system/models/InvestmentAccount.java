package com.bank.bank_management_system.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("INVESTMENT")
public class InvestmentAccount extends Account {

    @Override
    public String getAccountType() {
        return "INVESTMENT";
    }
}
