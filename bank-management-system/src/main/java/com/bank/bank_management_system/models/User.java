package com.bank.bank_management_system.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String email;
    private String password;
    private String accountNumber; // Unique 9-digit number

    // Constructor to auto-generate the account number
    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.accountNumber = generateAccountNumber(); // Automatically set the account number
    }

    // Method to generate a random 9-digit account number
    public static String generateAccountNumber() {
        return String.valueOf((long) (Math.random() * 900000000L) + 100000000L);
    }
}