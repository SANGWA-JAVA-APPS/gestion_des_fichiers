package com.bar.gestiondesfichier.common.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for standardized API responses and pagination
 */
public class ResponseUtil {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    /**
     * Create a successful response (200)
     */
    public static <T> ResponseEntity<Map<String, Object>> success(T data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Success");
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * Create a successful response with custom message
     */
    public static <T> ResponseEntity<Map<String, Object>> success(T data, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * Create a paginated success response
     */
    public static <T> ResponseEntity<Map<String, Object>> successWithPagination(Page<T> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Success");
        response.put("data", page.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("currentPage", page.getNumber());
        pagination.put("totalPages", page.getTotalPages());
        pagination.put("totalElements", page.getTotalElements());
        pagination.put("pageSize", page.getSize());
        pagination.put("hasNext", page.hasNext());
        pagination.put("hasPrevious", page.hasPrevious());
        
        response.put("pagination", pagination);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Create a bad request response (400)
     */
    public static ResponseEntity<Map<String, Object>> badRequest() {
        return badRequest("Check inputs");
    }

    public static ResponseEntity<Map<String, Object>> badRequest(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Create a forbidden response (403)
     */
    public static ResponseEntity<Map<String, Object>> forbidden() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 403);
        response.put("message", "Your session has expired, please login again");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Create default pageable with 20 records per page
     */
    public static Pageable createDefaultPageable() {
        return PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
    }

    /**
     * Create pageable with custom parameters
     */
    public static Pageable createPageable(Integer page, Integer size, String sort, String direction) {
        int pageNumber = page != null && page >= 0 ? page : DEFAULT_PAGE_NUMBER;
        int pageSize = size != null && size > 0 && size <= 100 ? size : DEFAULT_PAGE_SIZE;
        
        if (sort != null && !sort.isEmpty()) {
            Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            return PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sort));
        }
        
        return PageRequest.of(pageNumber, pageSize);
    }
}