package com.pico.budgetapplication.dto;

import java.time.LocalDateTime;

public class IncomeDTO {
    private Long id;
    private Integer amount;
    private LocalDateTime date;
    private String currency;
    private String desc;
    private String categoryName;

    public IncomeDTO() {
    }

    public IncomeDTO(Long id, Integer amount, LocalDateTime date, String currency, String desc, String categoryName) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.currency = currency;
        this.desc = desc;
        this.categoryName = categoryName;
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
}
