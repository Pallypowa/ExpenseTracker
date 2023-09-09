package com.pico.budgetapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "categoryName", length = 10)
    @NotBlank
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Expense> expenseList;
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Income> incomeList;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;
    public Category() {
    }

    public Category(String categoryName, String iconUrl, List<Expense> expenseList) {
        this.categoryName = categoryName;
        this.expenseList = expenseList;
    }

    public Category(String categoryName, List<Expense> expenseList) {
        this.categoryName = categoryName;
        this.expenseList = expenseList;
    }

    public Category(String categoryName, Long userId){
        this.categoryName = categoryName;
        this.user = new User();
        this.user.setId(userId);
    }

    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Expense> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean userIsNull(){
        return this.user == null;
    }
}
