package com.pico.budgetapplication.dto;

import java.util.UUID;

public class AccountDTO {
    private UUID id;
    private Integer balance;
    private String name;
    private String currency;

    public AccountDTO() {
    }

    public AccountDTO(UUID id, Integer balance, String name, String currency) {
        this.id = id;
        this.balance = balance;
        this.name = name;
        this.currency = currency;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
