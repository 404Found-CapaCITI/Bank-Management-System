package com.bank.bank_management_system.dto;

import lombok.Data;

@Data
public class AccountDto {
    private Long id;
    private String accountNumber;
    private double balance;
    private String accountType;
    private Long userId;
}