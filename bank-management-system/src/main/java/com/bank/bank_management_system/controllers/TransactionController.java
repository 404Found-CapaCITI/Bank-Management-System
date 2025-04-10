package com.bank.bank_management_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.bank_management_system.models.Transaction;
import com.bank.bank_management_system.services.TransactionService;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/history")
    public String showTransactionHistory(@RequestParam Long accountId, Model model) {
        List<Transaction> transactions = transactionService.getAccountTransactions(accountId);
        model.addAttribute("transactions", transactions);
        return "history"; // Separate history template or modal logic
    }
}