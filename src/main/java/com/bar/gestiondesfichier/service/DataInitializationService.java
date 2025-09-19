package com.bar.gestiondesfichier.service;

import com.bar.gestiondesfichier.entity.Account;
import com.bar.gestiondesfichier.entity.AccountCategory;
import com.bar.gestiondesfichier.repository.AccountCategoryRepository;
import com.bar.gestiondesfichier.repository.AccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializationService.class);
    private final AccountCategoryRepository accountCategoryRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    // Explicit constructor for dependency injection
    public DataInitializationService(AccountCategoryRepository accountCategoryRepository, 
                                   AccountRepository accountRepository,
                                   PasswordEncoder passwordEncoder) {
        this.accountCategoryRepository = accountCategoryRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization for File Management System...");

        // Create Account Categories
        AccountCategory adminCategory = createCategoryIfNotExists("ADMIN", "System Administrator Category");
        AccountCategory userCategory = createCategoryIfNotExists("USER", "Regular User Category");
        AccountCategory managerCategory = createCategoryIfNotExists("MANAGER", "File Manager Category");

        // Create Default Accounts
        createAccountIfNotExists("admin", "admin123", "admin@gestiondesfichier.com",
                "System Administrator", "+1234567890", adminCategory);

        createAccountIfNotExists("manager", "manager123", "manager@gestiondesfichier.com",
                "File Manager", "+1234567891", managerCategory);

        createAccountIfNotExists("user", "user123", "user@gestiondesfichier.com",
                "Regular User", "+1234567892", userCategory);

        log.info("Data initialization completed successfully!");
        log.info("Default users created:");
        log.info("- admin/admin123 (ADMIN)");
        log.info("- manager/manager123 (MANAGER)");
        log.info("- user/user123 (USER)");
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
                    // Encode password using BCrypt
                    String encodedPassword = passwordEncoder.encode(password);
                    Account account = new Account(username, encodedPassword, email, fullName, phoneNumber, "not specified", category);
                    Account saved = accountRepository.save(account);
                    log.info("Created account: {} ({}) with encoded password", username, category.getName());
                    return saved;
                });
    }
}