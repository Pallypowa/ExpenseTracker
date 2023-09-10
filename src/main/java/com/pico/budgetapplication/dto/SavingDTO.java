package com.pico.budgetapplication.dto;

import com.pico.budgetapplication.model.SavingTransHist;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class SavingDTO {
    private UUID id;
    private Integer goal;
    private Integer currentAmount;
    private LocalDate goalDate;
    private String title;
    private Boolean isFinished;
    private List<SavingTransHist> transactionHistory;

    public SavingDTO() {
    }

    public SavingDTO(UUID id, Integer goal, Integer currentAmount, LocalDate goalDate, String title, Boolean isFinished, List<SavingTransHist> transactionHistory) {
        this.id = id;
        this.goal = goal;
        this.currentAmount = currentAmount;
        this.goalDate = goalDate;
        this.title = title;
        this.isFinished = isFinished;
        this.transactionHistory = transactionHistory;
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

    public void setGoal(Integer goal) {
        this.goal = goal;
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

    public void setTransactionHistory(List<SavingTransHist> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }
}
