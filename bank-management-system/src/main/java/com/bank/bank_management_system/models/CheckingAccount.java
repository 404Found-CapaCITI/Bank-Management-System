package com.bank.bank_management_system.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CHECKING")
public class CheckingAccount extends Account {

    @Override
    public String getAccountType() {
        return "CHECKING";
    }
}
