package com.bank.bank_management_system.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.bank_management_system.models.Account;
import com.bank.bank_management_system.models.Transaction;
import com.bank.bank_management_system.services.AccountService;
import com.bank.bank_management_system.services.TransactionService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class DashboardApiController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    // This endpoint returns dashboard data: balance, transactions, and
    // accountNumber.
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData(HttpSession session) {
        String accountNumber = (String) session.getAttribute("accountNumber");
        if (accountNumber == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Account account = accountService.getAccountByNumber(accountNumber);
        if (account == null) {
            return ResponseEntity.badRequest().body("No account found for account number: " + accountNumber);
        }
        double balance = account.getBalance();
        // Get transactions ordered from newest to oldest.
        List<Transaction> transactions = transactionService.getAccountTransactions(account.getId());

        return ResponseEntity.ok(Map.of(
                "balance", balance,
                "transactions", transactions,
                "accountNumber", account.getAccountNumber()));
    }

    // Deposit funds into the main account.
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(HttpSession session, @RequestBody Map<String, String> payload) {
        String accountNumber = (String) session.getAttribute("accountNumber");
        if (accountNumber == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Account account = accountService.getAccountByNumber(accountNumber);
        if (account == null) {
            return ResponseEntity.badRequest().body("No account found for account number: " + accountNumber);
        }

        double amount = Double.parseDouble(payload.get("amount"));
        try {
            accountService.deposit(account.getId(), amount);
            Account updatedAccount = accountService.getAccountByNumber(account.getAccountNumber());
            return ResponseEntity.ok(Map.of("balance", updatedAccount.getBalance()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Deposit failed: " + e.getMessage());
        }
    }

    // Withdraw funds from the main account.
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(HttpSession session, @RequestBody Map<String, String> payload) {
        String accountNumber = (String) session.getAttribute("accountNumber");
        if (accountNumber == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Account account = accountService.getAccountByNumber(accountNumber);
        if (account == null) {
            return ResponseEntity.badRequest().body("No account found for account number: " + accountNumber);
        }

        double amount = Double.parseDouble(payload.get("amount"));
        try {
            accountService.withdraw(account.getId(), amount);
            Account updatedAccount = accountService.getAccountByNumber(account.getAccountNumber());
            return ResponseEntity.ok(Map.of("balance", updatedAccount.getBalance()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Withdrawal failed: " + e.getMessage());
        }
    }

    // Transfer funds from the main account to another account.
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(HttpSession session, @RequestBody Map<String, String> payload) {
        String accountNumber = (String) session.getAttribute("accountNumber");
        if (accountNumber == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Account account = accountService.getAccountByNumber(accountNumber);
        if (account == null) {
            return ResponseEntity.badRequest().body("No account found for account number: " + accountNumber);
        }

        double amount = Double.parseDouble(payload.get("amount"));
        String recipientAccount = payload.get("recipientAccount");
        try {
            accountService.transfer(account.getId(), recipientAccount, amount);
            Account updatedAccount = accountService.getAccountByNumber(account.getAccountNumber());
            return ResponseEntity.ok(Map.of("balance", updatedAccount.getBalance()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Transfer failed: " + e.getMessage());
        }
    }

    // Create a new account for the user.
    @PostMapping("/create-account")
    public ResponseEntity<?> createAccount(HttpSession session, @RequestBody Map<String, String> payload) {
        String accountNumber = (String) session.getAttribute("accountNumber");
        if (accountNumber == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // You might want to associate the new account with the user owning the current
        // account.
        // One way is to fetch the current account and then get its user.
        Account currentAccount = accountService.getAccountByNumber(accountNumber);
        if (currentAccount == null) {
            return ResponseEntity.badRequest().body("No account found for account number: " + accountNumber);
        }

        String accountType = payload.get("accountType");
        double initialDeposit = Double.parseDouble(payload.get("initialDeposit"));
        try {
            // Create a new account for the same user that owns the current account.
            Account newAccount = accountService.createAccount(currentAccount.getUser(), accountType, initialDeposit);
            // It’s a good idea to return minimal data (avoid lazy associations)
            Map<String, Object> responseData = Map.of(
                    "id", newAccount.getId(),
                    "accountNumber", newAccount.getAccountNumber(),
                    "balance", newAccount.getBalance());
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Account creation failed: " + e.getMessage());
        }
    }
}