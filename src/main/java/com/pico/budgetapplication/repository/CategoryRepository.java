package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByUserId(Long userId);
    void deleteByCategoryName(String categoryName);
    @Query("""
        SELECT c FROM Category c WHERE c.categoryName = :categoryName
""")
    Optional<List<Category>> findCatByName(@Param("categoryName") String categoryName);
}
