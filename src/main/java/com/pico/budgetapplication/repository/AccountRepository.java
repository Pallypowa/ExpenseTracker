package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.Account;
import com.pico.budgetapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findAllByUser(User user);
    @Query("""
        SELECT a FROM Account a WHERE a.user.id = :userId AND a.isMainAccount = true
""")
    Optional<Account> findMainAccount(@Param("userId") Long userId);
}
