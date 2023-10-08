package com.pico.budgetapplication.controller;

import com.pico.budgetapplication.dto.ExpenseDTO;
import com.pico.budgetapplication.dto.IncomeDTO;
import com.pico.budgetapplication.dto.SavingDTO;
import com.pico.budgetapplication.dto.SavingTransDTO;
import com.pico.budgetapplication.service.TransactionService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        try{
            IncomeDTO returnDTO = transactionService.addIncome(incomeDTO, principal);
            return ResponseEntity.ok(returnDTO);
        }catch (ResponseStatusException responseStatusException){
            return new ResponseEntity<>(responseStatusException.getMessage(), responseStatusException.getStatusCode());
        }catch (RuntimeException runtimeException){
            return new ResponseEntity<>(runtimeException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addExpense")
    public ResponseEntity<?> addExpense(@NotNull @RequestBody ExpenseDTO expenseDTO, Principal principal){
        try{
            ExpenseDTO returnDTO = transactionService.addExpense(expenseDTO, principal);
            return ResponseEntity.ok(returnDTO);
        }catch (ResponseStatusException responseStatusException){
            return new ResponseEntity<>(responseStatusException.getMessage(), responseStatusException.getStatusCode());
        }catch (RuntimeException runtimeException){
            return new ResponseEntity<>(runtimeException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @PostMapping("/addSaving")
    public ResponseEntity<?> addSaving(@NotNull @RequestBody SavingDTO savingDTO, Principal principal){
        SavingDTO createdSaving = transactionService.addSaving(savingDTO, principal);
        return ResponseEntity.ok(createdSaving);
    }
    @GetMapping("/mySavings")
    public ResponseEntity<?> findMySavings(Principal principal){
        List<SavingDTO> mySavings = transactionService.getMySavings(principal);
        return ResponseEntity.ok(mySavings);
    }

    @PostMapping("/addSavingTransaction")
    public ResponseEntity<?> addTransaction(@NotNull @RequestParam(name = "accountId") UUID accountId,
                                            @NotNull @RequestBody SavingTransDTO transaction,
                                            Principal principal){
        SavingTransDTO returnDTO = transactionService.addTransaction(accountId, transaction, principal);
        return ResponseEntity.ok(returnDTO);
    }

    @PutMapping("/updateSaving")
    public ResponseEntity<?> updateSaving(@NotNull @RequestBody SavingDTO savingDTO, Principal principal){
        SavingDTO returnDTO = transactionService.updateSaving(savingDTO, principal);
        return ResponseEntity.ok(returnDTO);
    }

    @DeleteMapping("/deleteSaving/{savingId}")
    public void deleteSaving(@NotNull @PathVariable(name = "savingId") UUID savingId, Principal principal){
        transactionService.deleteSaving(savingId, principal);
    }

    @PutMapping("/updateSavingTransHist")
    public ResponseEntity<?> updateSTransactionHistory(@NotNull @RequestBody SavingTransDTO transaction, Principal principal){
        SavingTransDTO returnDTO = transactionService.updateTransactionHistory(transaction, principal);
        return ResponseEntity.ok(returnDTO);
    }

    @DeleteMapping("deleteSavingTransHist/{savingTransHistId}")
    public void deleteStransactionHistory(@NotNull @PathVariable UUID savingTransHistId, Principal principal){
        transactionService.deleteSavingTransHist(savingTransHistId, principal);
    }
}
