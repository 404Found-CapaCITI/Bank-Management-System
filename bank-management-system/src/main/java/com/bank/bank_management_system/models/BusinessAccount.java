package com.bank.bank_management_system.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BUSINESS")
public class BusinessAccount extends Account {

    @Override
    public String getAccountType() {
        return "BUSINESS";
    }
}
