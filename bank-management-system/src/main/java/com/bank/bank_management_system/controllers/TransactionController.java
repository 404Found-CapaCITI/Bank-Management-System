package com.bank.bank_management_system.controllers;

import com.bank.bank_management_system.dto.*;
import com.bank.bank_management_system.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionDto>> getAccountTransactions(@PathVariable Long accountId) {
        List<TransactionDto> transactions = transactionService.getAccountTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(
            @RequestParam Long accountId,
            @RequestParam String type,
            @RequestParam double amount,
            @RequestParam(required = false) String description) {
        TransactionDto transactionDto = transactionService.createTransaction(accountId, type, amount, description);
        return ResponseEntity.ok(transactionDto);
    }
}