package com.igihecyubuntu.app.service;

import com.igihecyubuntu.app.entity.Account;
import com.igihecyubuntu.app.repository.AccountRepository;
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
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            String categoryName = account.getAccountCategory().getName();
            return User.builder()
                    .username(account.getUsername())
                    .password(account.getPassword())
                    .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + categoryName)
                    ))
                    .disabled(!account.isActive())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
