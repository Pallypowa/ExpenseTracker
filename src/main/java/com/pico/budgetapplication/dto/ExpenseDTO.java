package com.pico.budgetapplication.dto;

import com.pico.budgetapplication.model.PaymentMethod;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class ExpenseDTO {
    private Long id;
    @Positive
    private Integer amount;
    private LocalDateTime date;
    private String currency;
    private String desc;
    private String categoryName;
    private PaymentMethod paymentMethod;

    public ExpenseDTO() {
    }

    public ExpenseDTO(Long id, Integer amount, LocalDateTime date, String currency, String desc, String categoryName, PaymentMethod paymentMethod) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.currency = currency;
        this.desc = desc;
        this.categoryName = categoryName;
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
