package com.pico.budgetapplication.controller;

import com.pico.budgetapplication.dto.IncomeDTO;
import com.pico.budgetapplication.service.TransactionService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/addIncome")
    public ResponseEntity<?> addIncome(@NotNull @RequestParam Long accountId,
                                       @NotNull @RequestBody IncomeDTO incomeDTO,
                                       Principal principal){
        transactionService.addIncome(accountId, incomeDTO, principal);
        return ResponseEntity.ok(incomeDTO);
    }

    //TODO add other Expense/Income related methods here.
}
