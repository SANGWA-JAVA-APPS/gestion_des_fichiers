package com.bar.gestiondesfichier.interfaces;

/**
 * Interface for login request data
 */
public interface LoginRequest {
    String getUsername();
    String getPassword();
    void setUsername(String username);
    void setPassword(String password);
}