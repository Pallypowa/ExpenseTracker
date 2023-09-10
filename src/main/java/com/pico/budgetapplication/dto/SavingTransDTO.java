package com.pico.budgetapplication.dto;

import com.pico.budgetapplication.model.SavingTransType;

import java.time.LocalDateTime;
import java.util.UUID;

public class SavingTransDTO {
    private UUID id;
    private Integer amount;

    private SavingTransType transactionType;
    private LocalDateTime timestamp;

    private UUID savingId;

    public SavingTransDTO() {
    }

    public SavingTransDTO(UUID id, Integer amount, SavingTransType transactionType, LocalDateTime timestamp, UUID savingId) {
        this.id = id;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
        this.savingId = savingId;
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

    public SavingTransType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(SavingTransType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getSavingId() {
        return savingId;
    }

    public void setSavingId(UUID savingId) {
        this.savingId = savingId;
    }
}
