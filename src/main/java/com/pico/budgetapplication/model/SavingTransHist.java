package com.pico.budgetapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.UUID;

//Saving Transaction History
@Entity
public class SavingTransHist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Integer amount;
    @Enumerated(EnumType.ORDINAL)
    private SavingTransType transactionType;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name = "saving_id")
    @JsonIgnore
    private Saving saving;

    public SavingTransHist() {
    }

    public SavingTransHist(UUID id, Integer amount, SavingTransType transactionType, LocalDateTime timestamp, Saving saving) {
        this.id = id;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
        this.saving = saving;
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

    public Saving getSaving() {
        return saving;
    }

    public void setSaving(Saving saving) {
        this.saving = saving;
    }
}
