package com.bank.bank_management_system.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.bank_management_system.dto.AccountDto;
import com.bank.bank_management_system.exception.InsufficientBalanceException;
import com.bank.bank_management_system.exception.ResourceNotFoundException;
import com.bank.bank_management_system.models.Account;
import com.bank.bank_management_system.models.BusinessAccount;
import com.bank.bank_management_system.models.CheckingAccount;
import com.bank.bank_management_system.models.InvestmentAccount;
import com.bank.bank_management_system.models.SavingsAccount;
import com.bank.bank_management_system.models.User;
import com.bank.bank_management_system.repositories.AccountRepository;
import com.bank.bank_management_system.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccountDto createAccount(Long userId, String accountType, double initialDeposit) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Account account;
        switch (accountType.toUpperCase()) {
            case "SAVINGS" -> account = new SavingsAccount();
            case "CHECKING" -> account = new CheckingAccount();
            case "BUSINESS" -> account = new BusinessAccount();
            case "INVESTMENT" -> account = new InvestmentAccount();
            default -> throw new IllegalArgumentException("Invalid account type: " + accountType);
        }

        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);

        if (initialDeposit > 0) {
            account.deposit(initialDeposit);
        }

        Account savedAccount = accountRepository.save(account);
        return mapToDto(savedAccount);
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AccountDto getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        return mapToDto(account);
    }

    @Transactional
    public AccountDto deposit(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        account.deposit(amount);
        Account updatedAccount = accountRepository.save(account);
        return mapToDto(updatedAccount);
    }

    @Transactional
    public AccountDto withdraw(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        account.withdraw(amount);
        Account updatedAccount = accountRepository.save(account);
        return mapToDto(updatedAccount);
    }

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, double amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found with id: " + fromAccountId));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Destination account not found with id: " + toAccountId));

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        // Add transfer description to transactions
        fromAccount.getTransactions().get(fromAccount.getTransactions().size() - 1)
                .setDescription("Transfer to " + toAccount.getAccountNumber() + ": " + description);
        toAccount.getTransactions().get(toAccount.getTransactions().size() - 1)
                .setDescription("Transfer from " + fromAccount.getAccountNumber() + ": " + description);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    private AccountDto mapToDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setBalance(account.getBalance());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setUserId(account.getUser().getId());
        return accountDto;
    }
}