package com.pico.budgetapplication.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class IncomeDTO {
    private UUID id;
    private Integer amount;
    private LocalDateTime date;
    private String currency;
    private String desc;
    private String categoryName;
    private UUID accountId;

    public IncomeDTO() {
    }

    public IncomeDTO(UUID id, Integer amount, LocalDateTime date, String currency, String desc, String categoryName) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.currency = currency;
        this.desc = desc;
        this.categoryName = categoryName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }
}
