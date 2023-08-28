package com.pico.budgetapplication.dto;

import com.pico.budgetapplication.model.PaymentMethod;

import java.time.LocalDateTime;

public record ExpenseDTO(Long id,
                         Integer amount,
                         LocalDateTime addedAt,
                         String currency,
                         String desc,
                         String categoryName,
                         PaymentMethod paymentMethod) {
}
