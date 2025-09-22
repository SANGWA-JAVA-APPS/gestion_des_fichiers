package com.igihecyubuntu.app.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igihecyubuntu.app.dto.LoginRequest;
import com.igihecyubuntu.app.dto.LoginResponse;
import com.igihecyubuntu.app.dto.RegistrationRequest;
import com.igihecyubuntu.app.dto.RegistrationResponse;
import com.igihecyubuntu.app.entity.Account;
import com.igihecyubuntu.app.entity.AccountCategory;
import com.igihecyubuntu.app.repository.AccountCategoryRepository;
import com.igihecyubuntu.app.repository.AccountRepository;
import com.igihecyubuntu.app.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountCategoryRepository accountCategoryRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Optional<Account> accountOpt = accountRepository.findByUsername(loginRequest.getUsername());
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            boolean passwordMatches = false;
            if (account.getPassword().startsWith("$2")) {
                passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), account.getPassword());
            } else {
                passwordMatches = loginRequest.getPassword().equals(account.getPassword());
                if (passwordMatches) {
                    account.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
                    accountRepository.save(account);
                }
            }
            if (passwordMatches) {
                // Generate JWT tokens
                String token = jwtUtil.generateToken(account.getUsername(), account.getAccountCategory().getName(), account.getFullName());
                String refreshToken = jwtUtil.generateRefreshToken(account.getUsername());
                
                LoginResponse response = new LoginResponse();
                response.setSuccess(true);
                response.setMessage("Login successful");
                response.setUserId(account.getId());
                response.setUsername(account.getUsername());
                response.setFullName(account.getFullName());
                response.setRole(account.getAccountCategory().getName());
                response.setToken(token);
                response.setRefreshToken(refreshToken);
                response.setExpiresIn(jwtExpiration);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(401).body(new LoginResponse(false, "Invalid username or password"));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        SecurityContextHolder.clearContext();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        if (authentication != null && authentication.isAuthenticated()) {
            response.put("username", authentication.getName());
            response.put("authorities", authentication.getAuthorities());
            response.put("authenticated", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("authenticated", false);
            return ResponseEntity.status(401).body(response);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "Username is required"));
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "Password is required"));
        }
        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "Phone number is required"));
        }
        if (accountRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(false, "Username already exists"));
        }
        
        // Get the requested account category or default to CLIENT
        String categoryName = request.getAccountCategory() != null ? 
            request.getAccountCategory().toUpperCase() : "CLIENT";
        AccountCategory accountCategory = accountCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException(categoryName + " category not found"));
                
        Account newAccount = new Account(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail() != null ? request.getEmail() : request.getUsername() + "@example.com",
                request.getFullName() != null ? request.getFullName() : request.getUsername(),
                request.getPhoneNumber(),
                request.getGender(),
                accountCategory
        );
        Account savedAccount = accountRepository.save(newAccount);
        return ResponseEntity.ok(new RegistrationResponse(true, "Registration successful", savedAccount.getId()));
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            Optional<Account> accountOpt = accountRepository.findByUsername(username);
            
            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                String newToken = jwtUtil.generateToken(account.getUsername(), account.getAccountCategory().getName(), account.getFullName());
                String newRefreshToken = jwtUtil.generateRefreshToken(account.getUsername());
                
                LoginResponse response = new LoginResponse();
                response.setSuccess(true);
                response.setMessage("Token refreshed successfully");
                response.setUsername(account.getUsername());
                response.setFullName(account.getFullName());
                response.setRole(account.getAccountCategory().getName());
                response.setToken(newToken);
                response.setRefreshToken(newRefreshToken);
                response.setExpiresIn(jwtExpiration);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(401).body(new LoginResponse(false, "Invalid refresh token"));
    }
}
