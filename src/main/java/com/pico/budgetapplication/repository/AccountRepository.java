package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
