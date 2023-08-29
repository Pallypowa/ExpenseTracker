package com.pico.budgetapplication.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(@NotBlank String categoryName) {
}
