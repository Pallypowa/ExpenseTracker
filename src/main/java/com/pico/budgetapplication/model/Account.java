package com.pico.budgetapplication.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String accountName;
    private Integer balance;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String currency;

    public Account() {
    }

    public Account(String accountName, Integer balance, User user, String currency) {
        this.accountName = accountName;
        this.balance = balance;
        this.user = user;
        this.currency = currency;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Long getUserId(){
        return this.user.getId();
    }
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
