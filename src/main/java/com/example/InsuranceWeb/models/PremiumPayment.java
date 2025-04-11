package com.example.InsuranceWeb.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "premium_payments")
public class PremiumPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate paymentDate;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    public PremiumPayment() {}

    public PremiumPayment(LocalDate paymentDate, double amount, Purchase purchase) {
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.purchase = purchase;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
}
