
package com.bank.bank_management_system.repository;

import com.bank.bank_management_system.model.BankAccount;
import com.bank.bank_management_system.model.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOrderByDateTimeDesc(BankAccount account);
}