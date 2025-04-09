package com.bank.bank_management_system.services;

import com.bank.bank_management_system.dto.TransactionDto;
import com.bank.bank_management_system.exception.ResourceNotFoundException;
import com.bank.bank_management_system.models.Account;
import com.bank.bank_management_system.models.Transaction;
import com.bank.bank_management_system.repositories.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public List<TransactionDto> getAccountTransactions(Long accountId) {
        return transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TransactionDto createTransaction(Long accountId, String type, double amount, String description) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setAccount(account);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToDto(savedTransaction);
    }

    private TransactionDto mapToDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setType(transaction.getType());
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setTransactionDate(transaction.getTransactionDate());
        transactionDto.setAccountId(transaction.getAccount().getId());
        return transactionDto;
    }
}