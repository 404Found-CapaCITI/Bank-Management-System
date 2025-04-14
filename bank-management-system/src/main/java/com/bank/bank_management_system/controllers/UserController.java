package com.bank.bank_management_system.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.bank_management_system.services.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getUserInfo(HttpSession session) {
        String accountNumber = (String) session.getAttribute("accountNumber");

        if (accountNumber == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Not logged in"));
        }

        return userService.findByAccountNumber(accountNumber)
                .map(user -> {
                    Map<String, String> userData = new HashMap<>();
                    userData.put("name", user.getName());
                    userData.put("surname", user.getSurname());
                    return ResponseEntity.ok(userData);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "User not found")));
    }
}