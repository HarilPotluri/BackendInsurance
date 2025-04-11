package com.example.InsuranceWeb.models;

import jakarta.persistence.*;

@Entity
@Table(name = "policies")
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type; // Type of policy (e.g., Health, Life, Auto, Home)
    private double premium; // The cost of the policy
    
    @Column(length = 1000)
    private String description;


    public Policy() {}

    public Policy(String name, String type, double premium) {
        this.name = name;
        this.type = type;
        this.premium = premium;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getPremium() { return premium; }
    public void setPremium(double premium) { this.premium = premium; }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
