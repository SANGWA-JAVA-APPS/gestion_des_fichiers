package com.bar.gestiondesfichier.controller;

import com.bar.gestiondesfichier.dto.LoginRequestDto;
import com.bar.gestiondesfichier.dto.LoginResponseDto;
import com.bar.gestiondesfichier.entity.Account;
import com.bar.gestiondesfichier.repository.AccountRepository;
import com.bar.gestiondesfichier.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Tag(name = "user", description = "User Management API for authentication and user operations")
public class AuthController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user with username and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest, HttpServletRequest request) {
        System.out.println("=== LOGIN REQUEST DEBUG ===");
        System.out.println("Username: " + loginRequest.getUsername());
        System.out.println("Remote Address: " + request.getRemoteAddr());
        System.out.println("Origin Header: " + request.getHeader("Origin"));
        System.out.println("========================");

        try {
            Optional<Account> accountOpt = accountRepository.findByUsername(loginRequest.getUsername());

            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                System.out.println("Account found: " + account.getUsername());

                boolean passwordMatches = false;

                // Check if password is BCrypt encoded or plain text
                if (account.getPassword().startsWith("$2")) {
                    // BCrypt encoded password
                    passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), account.getPassword());
                } else {
                    // Plain text password (for backwards compatibility)
                    passwordMatches = loginRequest.getPassword().equals(account.getPassword());

                    // If login successful with plain text, encode and update the password
                    if (passwordMatches) {
                        account.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
                        accountRepository.save(account);
                        System.out.println("Updated plain text password to BCrypt for user: " + account.getUsername());
                    }
                }

                if (passwordMatches && account.isActive()) {
                    System.out.println("Login successful for user: " + account.getUsername());

                    // Generate JWT tokens
                    String jwtToken = jwtUtils.generateTokenFromUsername(account.getUsername());
                    String refreshToken = jwtUtils.generateRefreshToken(account.getUsername());

                    LoginResponseDto response = new LoginResponseDto();
                    response.setSuccess(true);
                    response.setMessage("Login successful");
                    response.setUsername(account.getUsername());
                    response.setFullName(account.getFullName());
                    response.setRole(account.getAccountCategory().getName());
                    response.setToken(jwtToken);
                    response.setRefreshToken(refreshToken);

                    return ResponseEntity.ok(response);
                } else {
                    System.out.println("Password does not match or account inactive for user: " + account.getUsername());
                }
            } else {
                System.out.println("Account not found for username: " + loginRequest.getUsername());
            }

            return ResponseEntity.status(401).body(new LoginResponseDto(false, "Invalid username or password"));
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(new LoginResponseDto(false, "Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logout the current user")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    @Operation(summary = "Get current user", description = "Get current authenticated user information")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User information retrieved"),
        @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
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
}