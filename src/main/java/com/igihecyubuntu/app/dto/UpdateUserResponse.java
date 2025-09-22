package com.igihecyubuntu.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResponse {
    private boolean success;
    private String message;
    private AccountDto user;
    
    public UpdateUserResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
