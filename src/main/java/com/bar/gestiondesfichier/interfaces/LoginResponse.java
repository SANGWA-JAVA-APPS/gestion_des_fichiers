package com.bar.gestiondesfichier.interfaces;

/**
 * Interface for login response data
 */
public interface LoginResponse {
    boolean isSuccess();
    String getMessage();
    String getUsername();
    String getFullName();
    String getRole();
    void setSuccess(boolean success);
    void setMessage(String message);
    void setUsername(String username);
    void setFullName(String fullName);
    void setRole(String role);
}