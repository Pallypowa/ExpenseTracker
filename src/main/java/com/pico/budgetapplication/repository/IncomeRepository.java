package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Integer> {
}
