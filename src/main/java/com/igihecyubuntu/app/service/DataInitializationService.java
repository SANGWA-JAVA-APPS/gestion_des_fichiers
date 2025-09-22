package com.igihecyubuntu.app.service;

import com.igihecyubuntu.app.entity.Account;
import com.igihecyubuntu.app.entity.AccountCategory;
import com.igihecyubuntu.app.repository.AccountCategoryRepository;
import com.igihecyubuntu.app.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService implements CommandLineRunner {

    private final AccountCategoryRepository accountCategoryRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");

        // Create Account Categories
        AccountCategory adminCategory = createCategoryIfNotExists("ADMIN", "System Administrator Category");
        createCategoryIfNotExists("CLIENT", "Client Category");
        createCategoryIfNotExists("MEMBER", "Church Member Category");

        // Create Default Admin Account
        createAccountIfNotExists("admin", "admin123", "admin@igihecyubuntu.com",
                "System Administrator", "+250788000000", adminCategory);

        log.info("Data initialization completed successfully!");
        log.info("Default users created:");
        log.info("- admin/admin123 (ADMIN)");
    }

    private AccountCategory createCategoryIfNotExists(String name, String description) {
        return accountCategoryRepository.findByName(name)
                .orElseGet(() -> {
                    AccountCategory category = new AccountCategory(name, description);
                    AccountCategory saved = accountCategoryRepository.save(category);
                    log.info("Created account category: {}", name);
                    return saved;
                });
    }

    private Account createAccountIfNotExists(String username, String password, String email,
            String fullName, String phoneNumber, AccountCategory category) {
        return accountRepository.findByUsername(username)
                .orElseGet(() -> {
                    String encodedPassword = passwordEncoder.encode(password);
                    Account account = new Account(username, encodedPassword, email, fullName, phoneNumber, "not specified", category);
                    Account saved = accountRepository.save(account);
                    log.info("Created account: {} ({}) with encoded password", username, category.getName());
                    return saved;
                });
    }
}
