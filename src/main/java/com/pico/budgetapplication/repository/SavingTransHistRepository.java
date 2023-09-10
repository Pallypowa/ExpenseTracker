package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.SavingTransHist;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SavingTransHistRepository extends JpaRepository<SavingTransHist, UUID> {
}
