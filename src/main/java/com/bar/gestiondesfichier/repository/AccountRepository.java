package com.bar.gestiondesfichier.repository;

import com.bar.gestiondesfichier.entity.Account;
import com.bar.gestiondesfichier.entity.AccountCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a JOIN FETCH a.accountCategory WHERE a.username = :username")
    Optional<Account> findByUsername(@Param("username") String username);
    Optional<Account> findByEmail(String email);
    List<Account> findByAccountCategory(AccountCategory accountCategory);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<Account> findByActiveTrue();
    
    @Query("SELECT a FROM Account a WHERE a.accountCategory.name = 'ADMIN' AND a.active = true")
    List<Account> findAllActiveAdmins();
    
    @Query("SELECT a FROM Account a WHERE a.accountCategory.name = 'USER' AND a.active = true")
    List<Account> findAllActiveUsers();
    
    // Additional methods for dashboard
    long countByAccountCategory_Name(String categoryName);
}