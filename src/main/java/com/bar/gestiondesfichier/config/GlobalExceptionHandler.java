package com.bar.gestiondesfichier.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for standardized error responses
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle 404 - Resource not found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoHandlerFoundException ex, WebRequest request) {
        log.warn("404 Not Found: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", 404);
        response.put("message", "The requested resource was not found on this server");
        response.put("path", ex.getRequestURL());
        response.put("method", ex.getHttpMethod());
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handle 400 - Bad Request
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex, WebRequest request) {
        log.warn("400 Bad Request: {}", ex.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("message", "Check inputs");
        response.put("details", ex.getMessage());
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle 403 - Access Denied / Session Expired
     */
    @ExceptionHandler({AccessDeniedException.class, BadCredentialsException.class})
    public ResponseEntity<Map<String, Object>> handleAccessDenied(Exception ex, WebRequest request) {
        log.warn("403 Access Denied: {}", ex.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", 403);
        response.put("message", "Your session has expired, please login again");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Handle general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex, WebRequest request) {
        log.error("500 Internal Server Error: {}", ex.getMessage(), ex);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("message", "An internal server error occurred");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}