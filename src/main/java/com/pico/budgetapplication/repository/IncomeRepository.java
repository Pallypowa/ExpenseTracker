package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.Income;
import com.pico.budgetapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IncomeRepository extends JpaRepository<Income, UUID> {
    List<Income> findAllByUser(User user);
}
