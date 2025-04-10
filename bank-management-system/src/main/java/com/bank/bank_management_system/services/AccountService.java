package com.bank.bank_management_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.bank_management_system.models.Account;
import com.bank.bank_management_system.models.BusinessAccount;
import com.bank.bank_management_system.models.CheckingAccount;
import com.bank.bank_management_system.models.CurrentAccount;
import com.bank.bank_management_system.models.InvestmentAccount;
import com.bank.bank_management_system.models.SavingsAccount;
import com.bank.bank_management_system.models.User;
import com.bank.bank_management_system.repositories.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(User user, String accountType, double initialBalance) {
        Account account;
        switch (accountType.toUpperCase()) {
            case "SAVINGS" -> account = new SavingsAccount(user, initialBalance);
            case "CHECKING" -> account = new CheckingAccount(user, initialBalance);
            case "INVESTMENT" -> account = new InvestmentAccount(user, initialBalance);
            case "BUSINESS" -> account = new BusinessAccount(user, initialBalance);
            case "CURRENT" -> // For completeness, though usually auto-created by User
                account = new CurrentAccount(user, initialBalance);
            default -> throw new IllegalArgumentException("Invalid account type: " + accountType);
        }
        return accountRepository.save(account);
    }

    public void deposit(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        account.deposit(amount);
        accountRepository.save(account);
    }

    public void withdraw(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        account.withdraw(amount);
        accountRepository.save(account);
    }

    public void transfer(Long fromAccountId, String toAccountNumber, double amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found with ID: " + fromAccountId));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Recipient account not found with number: " + toAccountNumber));
        fromAccount.transfer(toAccount, amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found with number: " + accountNumber));
    }
}