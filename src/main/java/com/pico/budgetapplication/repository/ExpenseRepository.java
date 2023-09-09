package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.Expense;
import com.pico.budgetapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    @Query("""
    SELECT e FROM Expense e WHERE e.category.id = :categoryId
""")
    List<Expense> findByCategory(@Param("categoryId") Integer id);
    List<Expense> findAllByUser(User user);

    List<Expense> findAllByDateBetween(LocalDateTime date, LocalDateTime date2);
}
