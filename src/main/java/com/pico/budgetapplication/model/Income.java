package com.pico.budgetapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Income {
    @Id
    @GeneratedValue
    private long Id;
    @Column(nullable = false)
    private Integer amount;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId")
    private Category category;
//    @ManyToOne
//    @JoinColumn(name = "accountId", nullable = false)
//    @JsonIgnore
//    private Account account;
}
