package com.bank.bank_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.bank_management_system.models.User;
import com.bank.bank_management_system.services.AccountService;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long accountId,
            @RequestParam double amount,
            Model model) {
        try {
            accountService.withdraw(accountId, amount);
            return "redirect:/dashboard?success=Withdrawal successful";
        } catch (Exception e) {
            model.addAttribute("error", "Withdrawal failed: " + e.getMessage());
            return "dashboard";
        }
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromAccountId,
            @RequestParam String toAccountNumber,
            @RequestParam double amount,
            Model model) {
        try {
            accountService.transfer(fromAccountId, toAccountNumber, amount);
            return "redirect:/dashboard?success=Transfer successful";
        } catch (Exception e) {
            model.addAttribute("error", "Transfer failed: " + e.getMessage());
            return "dashboard";
        }
    }

    @PostMapping("/create-account")
    public String createAccount(@RequestParam String accountType,
            @RequestParam double initialDeposit,
            Model model) {
        try {
            // Hardcoded user for now; replace with authenticated user
            User user = new User("Madela", "Read", "madela@example.com", "password");
            accountService.createAccount(user, accountType, initialDeposit);
            return "redirect:/dashboard?success=Account created";
        } catch (Exception e) {
            model.addAttribute("error", "Account creation failed: " + e.getMessage());
            return "dashboard";
        }
    }
}