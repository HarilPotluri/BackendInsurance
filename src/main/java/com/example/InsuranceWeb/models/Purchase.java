package com.example.InsuranceWeb.models;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyType;
    private double premium;
    private String purchaseDate;
    private String title;
    private String userEmail;

    private String fullName;
    private String dob;
    private String address;
    private String occupation;
    private String aadharFilePath;

    private String status; // PENDING, APPROVED, REJECTED
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;

    public Purchase() {}

    public Purchase(String policyType, double premium, String purchaseDate, String title,
                    String userEmail, String fullName, String dob, String address,
                    String occupation, String aadharFilePath, User user, Policy policy) {
        this.policyType = policyType;
        this.premium = premium;
        this.purchaseDate = purchaseDate;
        this.title = title;
        this.userEmail = userEmail;
        this.fullName = fullName;
        this.dob = dob;
        this.address = address;
        this.occupation = occupation;
        this.aadharFilePath = aadharFilePath;
        this.user = user;
        this.policy = policy;
        this.status = "PENDING";
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAadharFilePath() {
        return aadharFilePath;
    }

    public void setAadharFilePath(String aadharFilePath) {
        this.aadharFilePath = aadharFilePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

}
