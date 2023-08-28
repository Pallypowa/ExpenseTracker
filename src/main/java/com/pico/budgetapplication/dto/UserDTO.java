package com.pico.budgetapplication.dto;

import com.pico.budgetapplication.model.Gender;

public record UserDTO(String username,
                      String email,
                      String firstName,
                      String lastName,
                      Integer age,
                      Gender gender) {
}
