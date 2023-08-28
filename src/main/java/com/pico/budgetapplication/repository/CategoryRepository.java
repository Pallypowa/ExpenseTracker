package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserId(Long userId);
    void deleteByCategoryName(String categoryName);
    Optional<Category> findCategoryByCategoryNameAndUserId(String categoryName, Long userId);
}
