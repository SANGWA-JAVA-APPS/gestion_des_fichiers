package com.igihecyubuntu.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {
    private boolean success;
    private String message;
    private Long userId;
    public RegistrationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
