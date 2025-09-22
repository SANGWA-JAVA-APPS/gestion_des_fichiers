package com.bar.gestiondesfichier.dto;

import com.bar.gestiondesfichier.interfaces.LoginRequest;

public class LoginRequestImpl implements LoginRequest {
    private String username;
    private String password;

    // Default constructor
    public LoginRequestImpl() {}

    // All-args constructor
    public LoginRequestImpl(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}