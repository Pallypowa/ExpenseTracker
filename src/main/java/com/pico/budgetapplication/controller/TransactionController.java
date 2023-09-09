package com.pico.budgetapplication.controller;

import com.pico.budgetapplication.dto.ExpenseDTO;
import com.pico.budgetapplication.dto.IncomeDTO;
import com.pico.budgetapplication.service.TransactionService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/myIncomes")
    public ResponseEntity<List<IncomeDTO>> findMyIncomes(Principal principal){
        List<IncomeDTO> incomes = transactionService.findMyIncomes(principal);
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("/myExpenses")
    public ResponseEntity<List<ExpenseDTO>> findMyExpenses(Principal principal){
        List<ExpenseDTO> expenses = transactionService.findMyExpenses(principal);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping("/addIncome")
    public ResponseEntity<?> addIncome(@NotNull @RequestBody IncomeDTO incomeDTO, Principal principal){
        IncomeDTO returnDTO = transactionService.addIncome(incomeDTO, principal);
        return ResponseEntity.ok(returnDTO);
    }

    @PostMapping("/addExpense")
    public ResponseEntity<?> addExpense(@NotNull @RequestBody ExpenseDTO expenseDTO, Principal principal){
        ExpenseDTO returnDTO = transactionService.addExpense(expenseDTO, principal);
        return ResponseEntity.ok(returnDTO);
    }

    @PutMapping("/updateIncome")
    public ResponseEntity<?> updateIncome(@NotNull @RequestBody IncomeDTO incomeDTO, Principal principal){
        IncomeDTO returnDTO = transactionService.updateIncome(incomeDTO, principal);
        return ResponseEntity.ok(returnDTO);
    }
    @PutMapping("/updateExpense")
    public ResponseEntity<?> updateExpense(@NotNull @RequestBody ExpenseDTO expenseDTO, Principal principal){
        ExpenseDTO returnDTO = transactionService.updateExpense(expenseDTO, principal);
        return ResponseEntity.ok(returnDTO);
    }

    @DeleteMapping("/deleteIncome/{id}")
    public ResponseEntity<?> deleteIncome(@NotNull @PathVariable UUID id, Principal principal){
        transactionService.deleteIncome(id, principal);
        return ResponseEntity.ok("Deleted");
    }
    @DeleteMapping("/deleteExpense/{id}")
    public ResponseEntity<?> deleteExpense(@NotNull @PathVariable UUID id, Principal principal){
        transactionService.deleteExpense(id, principal);
        return ResponseEntity.ok("Deleted");
    }
    @GetMapping("/filterExpenses")
    public List<ExpenseDTO> filterExpenses(
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate,
            @RequestParam Map<String, String> requestParams, Principal principal){
        return transactionService.filterExpenses(startDate, endDate, requestParams, principal);
    }
    //TODO do a similar filtering for incomes, do a generate report etc...
}
