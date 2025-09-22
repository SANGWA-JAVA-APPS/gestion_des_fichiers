package com.igihecyubuntu.app.exception;

public class BadRequestException extends CustomException {
    
    public BadRequestException(String message) {
        super(message, 400);
    }
    
    public BadRequestException() {
        super("Invalid inputs", 400);
    }
}