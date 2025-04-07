
package com.bank.bank_management_system.controller;


import com.bank.bank_management_system.dto.*;
import com.bank.bank_management_system.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {
    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountCreationDTO accountDTO) {
        AccountResponseDTO account = bankAccountService.createAccount(accountDTO);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<AccountResponseDTO> deposit(
            @PathVariable String accountNumber,
            @RequestParam double amount) {
        AccountResponseDTO account = bankAccountService.deposit(accountNumber, amount);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<AccountResponseDTO> withdraw(
            @PathVariable String accountNumber,
            @RequestParam double amount) {
        AccountResponseDTO account = bankAccountService.withdraw(accountNumber, amount);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(
            @PathVariable String accountNumber) {
        List<TransactionDTO> transactions = bankAccountService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(transactions);
    }
}