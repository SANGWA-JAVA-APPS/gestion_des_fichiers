package com.bar.gestiondesfichier.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account_categories")
public class AccountCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @OneToMany(mappedBy = "accountCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "accountCategory", "password"})
    private List<Account> accounts = new ArrayList<>();
    
    // Default constructor (required by Hibernate)
    public AccountCategory() {}
    
    // Constructor with all fields
    public AccountCategory(Long id, String name, String description, List<Account> accounts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.accounts = accounts != null ? accounts : new ArrayList<>();
    }
    
    // Constructor for creating new categories
    public AccountCategory(String name, String description) {
        this.name = name;
        this.description = description;
        this.accounts = new ArrayList<>();
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Account> getAccounts() { return accounts; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }
}