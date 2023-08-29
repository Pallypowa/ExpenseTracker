package com.pico.budgetapplication.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateDTO(@NotBlank String oldName, @NotBlank String newName) {
}
