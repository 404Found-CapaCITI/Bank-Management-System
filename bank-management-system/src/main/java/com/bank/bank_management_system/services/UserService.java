package com.bank.bank_management_system.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.bank_management_system.models.CurrentAccount;
import com.bank.bank_management_system.models.User;
import com.bank.bank_management_system.repositories.AccountRepository;
import com.bank.bank_management_system.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(User user, String confirmPassword) {
        if (!user.getPassword().equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Generate account number
        user.setAccountNumber(User.generateAccountNumber());

        user.setPassword(passwordEncoder.encode(user.getPassword())); // hash password

        // Save the user
        User savedUser = userRepository.save(user);

        // Create a new CurrentAccount for the new user
        CurrentAccount currentAccount = new CurrentAccount(savedUser, 0.0);

        // Persist the account in the accounts table
        accountRepository.save(currentAccount);
        accountRepository.flush(); // Force flush to DB

        System.out.println("CurrentAccount saved for account number: " + savedUser.getAccountNumber());

        return savedUser;
    }

    public Optional<User> loginUser(String accountNumber, String rawPassword) {
        Optional<User> userOpt = userRepository.findByAccountNumber(accountNumber);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String accountNumber) throws UsernameNotFoundException {
        User user = userRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getAccountNumber(),
                user.getPassword(),
                new ArrayList<>() // empty authorities
        );
    }

    public Optional<User> findByAccountNumber(String accountNumber) {
        return userRepository.findByAccountNumber(accountNumber);
    }
}