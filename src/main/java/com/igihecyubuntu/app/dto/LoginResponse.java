package com.igihecyubuntu.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private Long userId;
    private String username;
    private String fullName;
    private String role;
    private String token;
    private String refreshToken;
    private Long expiresIn;
    
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
