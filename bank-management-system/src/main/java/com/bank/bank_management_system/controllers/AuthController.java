package com.bank.bank_management_system.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.bank_management_system.models.User;
import com.bank.bank_management_system.services.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        try {
            String name = request.get("name");
            String surname = request.get("surname");
            String email = request.get("email");
            String password = request.get("password");
            String confirmPassword = request.get("confirmPassword");

            User user = new User();
            user.setName(name);
            user.setSurname(surname);
            user.setEmail(email);
            user.setPassword(password);

            User registeredUser = userService.registerUser(user, confirmPassword);

            response.put("message", "User registered successfully");
            response.put("accountNumber", registeredUser.getAccountNumber());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            // Return JSON containing the error message for front-end handling
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request, HttpSession session) {
        String accountNumber = request.get("accountNumber");
        String password = request.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(accountNumber, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            session.setAttribute("accountNumber", accountNumber);
            System.out.println("Login successful. Session ID = " + session.getId()
                    + " | accountNumber = " + accountNumber);

            return ResponseEntity.ok("Login successful");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }
}