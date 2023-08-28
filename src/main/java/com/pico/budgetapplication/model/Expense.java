package com.pico.budgetapplication.model;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Expense")
public class Expense {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Integer amount;
    @Column(nullable = false)
    private LocalDateTime date;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private String desc;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId")
    private Category category;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    public Expense() {

    }
    public Expense(Integer amount,
                   LocalDateTime date,
                   String currency,
                   String desc) {
        this.amount = amount;
        this.date = date;
        this.currency = currency;
        this.desc = desc;
    }
    public Expense(Integer amount,
                   LocalDateTime date,
                   String currency,
                   String desc,
                   User user,
                   Category category) {
        this.amount = amount;
        this.date = date;
        this.currency = currency;
        this.desc = desc;
        this.user = user;
        this.category = category;
    }



    public Expense(Integer amount, LocalDateTime date, String currency, String desc, User user, Category category, PaymentMethod paymentMethod) {
        this.amount = amount;
        this.date = date;
        this.currency = currency;
        this.desc = desc;
        this.user = user;
        this.category = category;
        this.paymentMethod = paymentMethod;
    }

    public Expense(Long id, Integer amount, LocalDateTime date, String currency, String desc) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.currency = currency;
        this.desc = desc;
    }

    public Long getId() {
        return id;
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

    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return user.getId();
    }
    public Long getCategoryId(){ return category.getId(); }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
