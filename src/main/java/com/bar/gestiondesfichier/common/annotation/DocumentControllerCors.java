package com.bar.gestiondesfichier.common.annotation;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for centralized CORS configuration
 * Applied to all document-related controllers
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:5173", 
    "http://localhost:4173", 
    "http://127.0.0.1:3000",
    "http://127.0.0.1:5173",
    "http://127.0.0.1:4173"
}, allowedHeaders = "*", methods = {})
public @interface DocumentControllerCors {
}