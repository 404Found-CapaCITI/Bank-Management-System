/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bank.bank_management_system.service;



import com.bank.bank_management_system.dto.AccountResponseDTO;

import com.bank.bank_management_system.dto.AccountCreationDTO;
import com.bank.bank_management_system.dto.TransactionDTO;
import com.bank.bank_management_system.exception.*;
import com.bank.bank_management_system.model.*;
import com.bank.bank_management_system.repository.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository accountRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public AccountResponseDTO createAccount(AccountCreationDTO accountDTO) {
        if (accountRepository.existsByEmail(accountDTO.getEmail())) {
            throw new InvalidAmountException("An account with this email already exists");
        }
        
        String fullName = accountDTO.getFirstName() + " " + accountDTO.getLastName();
        AccountType accountType = AccountType.valueOf(accountDTO.getAccountType().toUpperCase());
        
        if (accountDTO.getInitialDeposit() < 0) {
            throw new InvalidAmountException("Initial deposit cannot be negative");
        }
        
        BankAccount account = new BankAccount(
            fullName,
            accountDTO.getInitialDeposit(),
            accountType,
            accountDTO.getEmail()
        );
        
        if (accountDTO.getInitialDeposit() > 0) {
            Transaction transaction = new Transaction(
                accountDTO.getInitialDeposit(),
                "Initial deposit",
                TransactionType.DEPOSIT,
                account
            );
            account.getTransactions().add(transaction);
        }
        
        BankAccount savedAccount = accountRepository.save(account);
        return convertToAccountResponseDTO(savedAccount);
    }

    @Transactional
    public AccountResponseDTO deposit(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive");
        }
        
        BankAccount account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        
        account.setBalance(account.getBalance() + amount);
        
        Transaction transaction = new Transaction(
            amount,
            "Deposit",
            TransactionType.DEPOSIT,
            account
        );
        transactionRepository.save(transaction);
        
        BankAccount updatedAccount = accountRepository.save(account);
        return convertToAccountResponseDTO(updatedAccount);
    }

    @Transactional
    public AccountResponseDTO withdraw(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
        
        BankAccount account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient funds for this withdrawal");
        }
        
        account.setBalance(account.getBalance() - amount);
        
        Transaction transaction = new Transaction(
            amount,
            "Withdrawal",
            TransactionType.WITHDRAWAL,
            account
        );
        transactionRepository.save(transaction);
        
        BankAccount updatedAccount = accountRepository.save(account);
        return convertToAccountResponseDTO(updatedAccount);
    }

    public List<TransactionDTO> getTransactionHistory(String accountNumber) {
        BankAccount account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        
        List<Transaction> transactions = transactionRepository.findByAccountOrderByDateTimeDesc(account);
        return transactions.stream()
            .map(this::convertToTransactionDTO)
            .collect(Collectors.toList());
    }

    private AccountResponseDTO convertToAccountResponseDTO(BankAccount account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountHolderName(account.getAccountHolderName());
        dto.setBalance(account.getBalance());
        dto.setAccountType(account.getAccountType().name());
        dto.setEmail(account.getEmail());
        return dto;
    }

    private TransactionDTO convertToTransactionDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setDateTime(transaction.getDateTime());
        dto.setType(transaction.getType().name());
        return dto;
    }
}