
package com.bank.bank_management_system.repository;

import com.bank.bank_management_system.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findByAccountNumber(String accountNumber);
    boolean existsByEmail(String email);
}