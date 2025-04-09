package com.bank.bank_management_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.bank_management_system.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdOrderByTransactionDateDesc(Long accountId);
}