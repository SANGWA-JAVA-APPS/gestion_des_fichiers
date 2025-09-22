package com.igihecyubuntu.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
    private String email;
    private String fullName;
    private String accountCategory = "CLIENT";
    private String gender;
    public RegistrationRequest(String username, String password, String confirmPassword, String phoneNumber, String email, String fullName) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.fullName = fullName;
    }
    public RegistrationRequest(String username, String email, String fullName, String gender, String password) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.gender = gender;
        this.password = password;
    }
}
