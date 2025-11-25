package com.qeep.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    // Default coins & pet value
    @Column(nullable = false)
    private int coins = 100;

    @Column(nullable = false)
    private int petValue = 20;

    // Who owns this user as a pet (null = free)
    @Column(name = "current_owner_id", nullable = true)
    private Long currentOwnerId;

    // Daily spin control — MUST BE String OR LocalDate (we use String for safety)
    @Column(name = "last_spin_date", length = 20)
    private String lastSpinDate;   // ← CHANGED TO String (recommended & safe)

    // Account created time
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Optional: Prevent update on creation
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}