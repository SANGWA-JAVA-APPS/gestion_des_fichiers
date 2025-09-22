package com.igihecyubuntu.app.service;

import com.igihecyubuntu.app.dto.UpdateUserRequest;
import com.igihecyubuntu.app.dto.ChangePasswordRequest;
import com.igihecyubuntu.app.entity.Account;
import com.igihecyubuntu.app.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
    
    public Account updateUser(String username, UpdateUserRequest request) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            
            if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
                account.setFullName(request.getFullName().trim());
            }
            
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                // Check if email is already taken by another user
                Optional<Account> existingEmailAccount = accountRepository.findByEmail(request.getEmail());
                if (existingEmailAccount.isPresent() && !existingEmailAccount.get().getUsername().equals(username)) {
                    throw new RuntimeException("Email is already in use by another account");
                }
                account.setEmail(request.getEmail().trim());
            }
            
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty()) {
                account.setPhoneNumber(request.getPhoneNumber().trim());
            }
            
            if (request.getGender() != null && !request.getGender().trim().isEmpty()) {
                account.setGender(request.getGender().toLowerCase().trim());
            }
            
            return accountRepository.save(account);
        }
        throw new RuntimeException("User not found");
    }
    
    public boolean changePassword(String username, ChangePasswordRequest request) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            
            // Verify current password
            if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }
            
            // Validate new password
            if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
                throw new RuntimeException("New password must be at least 6 characters long");
            }
            
            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                throw new RuntimeException("New password and confirmation do not match");
            }
            
            // Update password
            account.setPassword(passwordEncoder.encode(request.getNewPassword()));
            accountRepository.save(account);
            return true;
        }
        throw new RuntimeException("User not found");
    }
}
