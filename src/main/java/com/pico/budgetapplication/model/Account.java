package com.pico.budgetapplication.model;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String accountName;
    private Integer balance;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Account() {
    }

    public Account(Long id, String accountName, Integer balance, User user) {
        this.id = id;
        this.accountName = accountName;
        this.balance = balance;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
