package com.bar.gestiondesfichier.service;

import com.bar.gestiondesfichier.entity.Account;
import com.bar.gestiondesfichier.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== CustomUserDetailsService ===");
        System.out.println("Loading user: " + username);
        
        // Debug: List all users in database
        try {
            long totalUsers = accountRepository.count();
            System.out.println("Total users in database: " + totalUsers);
        } catch (Exception e) {
            System.out.println("Error counting users: " + e.getMessage());
        }
        
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            
            // Eagerly load the AccountCategory to avoid LazyInitializationException
            String categoryName = account.getAccountCategory().getName();
            
            System.out.println("Found user: " + account.getUsername() + 
                             " with role: " + categoryName +
                             " active: " + account.isActive());
            
            // Create UserDetails with the account information
            return User.builder()
                    .username(account.getUsername())
                    .password(account.getPassword())
                    .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + categoryName)
                    ))
                    .disabled(!account.isActive())
                    .build();
        } else {
            System.out.println("User not found in database: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}