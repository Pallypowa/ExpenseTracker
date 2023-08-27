package com.pico.budgetapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "ExpenseCategory")
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "categoryName")
    @NotBlank
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Expense> expenseList;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
