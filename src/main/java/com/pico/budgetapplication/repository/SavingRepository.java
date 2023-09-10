package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.Saving;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SavingRepository extends JpaRepository<Saving, UUID> {
    List<Saving> findAllByUserId(Long userId);
}
