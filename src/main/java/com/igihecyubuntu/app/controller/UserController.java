package com.igihecyubuntu.app.controller;

import com.igihecyubuntu.app.dto.*;
import com.igihecyubuntu.app.entity.Account;
import com.igihecyubuntu.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/profile")
    public ResponseEntity<AccountDto> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        Optional<Account> accountOpt = userService.findByUsername(username);
        
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            AccountDto accountDto = new AccountDto(
                account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getFullName(),
                account.getPhoneNumber(),
                account.isActive(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                account.getAccountCategory().getName()
            );
            return ResponseEntity.ok(accountDto);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/profile")
    public ResponseEntity<UpdateUserResponse> updateUser(
            @RequestBody UpdateUserRequest request, 
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(new UpdateUserResponse(false, "Authentication required"));
        }
        
        try {
            String username = authentication.getName();
            Account updatedAccount = userService.updateUser(username, request);
            
            AccountDto accountDto = new AccountDto(
                updatedAccount.getId(),
                updatedAccount.getUsername(),
                updatedAccount.getEmail(),
                updatedAccount.getFullName(),
                updatedAccount.getPhoneNumber(),
                updatedAccount.isActive(),
                updatedAccount.getCreatedAt(),
                updatedAccount.getUpdatedAt(),
                updatedAccount.getAccountCategory().getName()
            );
            
            return ResponseEntity.ok(new UpdateUserResponse(true, "User profile updated successfully", accountDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UpdateUserResponse(false, e.getMessage()));
        }
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(new ChangePasswordResponse(false, "Authentication required"));
        }
        
        try {
            String username = authentication.getName();
            userService.changePassword(username, request);
            return ResponseEntity.ok(new ChangePasswordResponse(true, "Password changed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ChangePasswordResponse(false, e.getMessage()));
        }
    }
}
