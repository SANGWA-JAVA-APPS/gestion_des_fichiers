package com.igihecyubuntu.app.controller;

import com.igihecyubuntu.app.dto.AccountDto;
import com.igihecyubuntu.app.entity.Account;
import com.igihecyubuntu.app.repository.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountRepository accountRepository;
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountDto> accountDtos = accounts.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(accountDtos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        return accountRepository.findById(id)
            .map(this::convertToDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<AccountDto> getAccountByUsername(@PathVariable String username) {
        return accountRepository.findByUsername(username)
            .map(this::convertToDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/active")
    public ResponseEntity<List<AccountDto>> getActiveAccounts() {
        List<Account> accounts = accountRepository.findByActiveTrue();
        List<AccountDto> accountDtos = accounts.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(accountDtos);
    }
    private AccountDto convertToDto(Account account) {
        return new AccountDto(
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
    }
}
