package com.bank.bank_management_system.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    // Redirect root URL to login page
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login.html";
    }
}