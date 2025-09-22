package com.igihecyubuntu.app.exception;

public class UnauthorizedException extends CustomException {
    
    public UnauthorizedException(String message) {
        super(message, 401);
    }
    
    public UnauthorizedException() {
        super("Unauthorized", 401);
    }
}