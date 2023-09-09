package com.pico.budgetapplication.dto;

import com.pico.budgetapplication.model.Gender;

public record RegistrationDTO(String username,
                              String password,
                              String email,
                              String firstName,
                              String lastName,
                              Integer age,
                              Gender gender,
                              String currency){
}
