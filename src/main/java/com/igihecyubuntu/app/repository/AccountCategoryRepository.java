package com.igihecyubuntu.app.repository;

import com.igihecyubuntu.app.entity.AccountCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountCategoryRepository extends JpaRepository<AccountCategory, Long> {
    Optional<AccountCategory> findByName(String name);
}
