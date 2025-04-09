package com.bank.bank_management_system.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;
    private double amount;
    private String type;
    private String description;
    private LocalDateTime transactionDate;
    private Long accountId;
}