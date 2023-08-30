package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.Expense;
import com.pico.budgetapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query("""
    SELECT e FROM Expense e WHERE e.category.id = :categoryId
""")
    List<Expense> findByCategory(@Param("categoryId") Integer id);
    List<Expense> findAllByUser(User user);

    List<Expense> findAllByDateBetweenAndUserId(LocalDateTime date, LocalDateTime date2, Long userId);
}
