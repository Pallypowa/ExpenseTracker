package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.Account;
import com.pico.budgetapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findAllByUser(User user);
}
