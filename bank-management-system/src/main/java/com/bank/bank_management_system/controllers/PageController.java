package com.bank.bank_management_system.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    // Redirect root URL to login page
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login.html";
    }

    // Serve dashboard only if logged in
    @GetMapping("/dashboard")
    public String serveDashboard(HttpSession session) {
        String accountNumber = (String) session.getAttribute("accountNumber");
        System.out.println("Dashboard access: Session ID = " + session.getId()
                + " | accountNumber = " + accountNumber);
        if (accountNumber == null) {
            return "redirect:/login.html";
        }
        return "forward:/dashboard.html";
    }
}