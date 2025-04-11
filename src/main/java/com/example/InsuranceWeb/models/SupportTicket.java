package com.example.InsuranceWeb.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String subject;
    private String message;
    private boolean resolved;

    private LocalDateTime createdAt;

    public SupportTicket() {}

    public SupportTicket(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.resolved = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getSubject() { return subject; }
    public String getMessage() { return message; }
    public boolean isResolved() { return resolved; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setMessage(String message) { this.message = message; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
