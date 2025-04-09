package com.bank.bank_management_system.controllers;

import com.bank.bank_management_system.dto.*;
import com.bank.bank_management_system.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(
            @RequestParam Long userId,
            @RequestParam String accountType,
            @RequestParam(required = false, defaultValue = "0") double initialDeposit) {
        AccountDto accountDto = accountService.createAccount(userId, accountType, initialDeposit);
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountDto>> getUserAccounts(@PathVariable Long userId) {
        List<AccountDto> accounts = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long accountId) {
        AccountDto accountDto = accountService.getAccountById(accountId);
        return ResponseEntity.ok(accountDto);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<AccountDto> deposit(
            @PathVariable Long accountId,
            @RequestParam double amount) {
        AccountDto accountDto = accountService.deposit(accountId, amount);
        return ResponseEntity.ok(accountDto);
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<AccountDto> withdraw(
            @PathVariable Long accountId,
            @RequestParam double amount) {
        AccountDto accountDto = accountService.withdraw(accountId, amount);
        return ResponseEntity.ok(accountDto);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam double amount,
            @RequestParam(required = false) String description) {
        accountService.transfer(fromAccountId, toAccountId, amount, description);
        return ResponseEntity.ok().build();
    }
}