package com.igihecyubuntu.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordResponse {
    private boolean success;
    private String message;
}
