package com.pico.budgetapplication.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
public class Saving {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private Integer goal;
    @Column(nullable = false)
    private Integer currentAmount;
    @Column(nullable = false)
    private LocalDate goalDate;
    @Column(nullable = false)
    private String title;
    @OneToMany(mappedBy = "saving", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SavingTransHist> transactionHistory;
    //Future: store images for goals
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Saving(UUID id, Integer goalAmount, Integer currentAmount, LocalDate goalDate, String title, List<SavingTransHist> transactions, User user) {
        this.id = id;
        this.goal = goalAmount;
        this.currentAmount = currentAmount;
        this.goalDate = goalDate;
        this.title = title;
        this.transactionHistory = transactions;
        this.user = user;
    }

    public Saving() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goalAmount) {
        this.goal = goalAmount;
    }

    public Integer getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Integer currentAmount) {
        this.currentAmount = currentAmount;
    }

    public LocalDate getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(LocalDate goalDate) {
        this.goalDate = goalDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SavingTransHist> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<SavingTransHist> transactions) {
        this.transactionHistory = transactions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
