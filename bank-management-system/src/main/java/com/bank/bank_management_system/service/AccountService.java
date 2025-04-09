package com.bank.bank_management_system.service;

import com.bank.bank_management_system.dto.*;
import com.bank.bank_management_system.exception.*;
import com.bank.bank_management_system.repository.*;
import com.bank.bank_management_system.model.*;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountDto createAccount(Long userId, String accountType, double initialDeposit) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Account account;
        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                account = new SavingsAccount();
                break;
            case "CHECKING":
                account = new CheckingAccount();
                break;
            case "BUSINESS":
                account = new BusinessAccount();
                break;
            case "INVESTMENT":
                account = new InvestmentAccount();
                break;
            default:
                throw new IllegalArgumentException("Invalid account type: " + accountType);
        }

        account.setAccountNumber(generateAccountNumber());
        account.setBalance(initialDeposit);
        account.setUser(user);
        
        Account savedAccount = accountRepository.save(account);
        return mapToDto(savedAccount);
    }

    public List<AccountDto> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AccountDto getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        return mapToDto(account);
    }

    public AccountDto deposit(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        
        account.deposit(amount);
        Account updatedAccount = accountRepository.save(account);
        return mapToDto(updatedAccount);
    }

    public AccountDto withdraw(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        
        account.withdraw(amount);
        Account updatedAccount = accountRepository.save(account);
        return mapToDto(updatedAccount);
    }

    public void transfer(Long fromAccountId, Long toAccountId, double amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found with id: " + fromAccountId));
        
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found with id: " + toAccountId));

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
        
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
