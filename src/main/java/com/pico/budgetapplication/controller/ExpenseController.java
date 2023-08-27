package com.pico.budgetapplication.controller;


import com.pico.budgetapplication.model.Expense;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.ExpenseRepository;
import com.pico.budgetapplication.service.ExpenseService;
import com.pico.budgetapplication.service.ServiceUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/expense_tracker/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    //Should be an admin endpoint
//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/findAll")
//    public List<Expense> findAll(){
//        return expenseRepository.findAll();
//    }

    @GetMapping("/findMyExpenses")
    public List<Expense> findMyExpenses(Principal principal){
        return expenseService.findUserExpenses(principal);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findById")
    public Optional<Expense> findById(@RequestParam(name = "id") Integer id, Principal principal){
        return expenseService.findById(id, principal);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/createExpense")
    public ResponseEntity<?> saveExpense(@RequestBody Expense expense, Principal principal){
        Expense returnExpense = expenseService.save(expense, principal);
        return new ResponseEntity<>(returnExpense, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/createExpensesList")
    public ResponseEntity<?> saveAllExpenses(@RequestBody List<Expense> expenseList, Principal principal){
        List<Expense> returnExpenses = expenseService.saveAllExpenses(expenseList, principal);
        return new ResponseEntity<>(returnExpenses, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/updateExpense")
    public ResponseEntity<?> updateExpense(@RequestBody Expense expense, Principal principal){
        Expense returnExpense = expenseService.update(expense, principal);
        return new ResponseEntity<>(returnExpense, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/deleteExpense")
    public void deleteExpense(@RequestParam(name = "id") Integer id, Principal principal){
        expenseService.delete(id, principal);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filter")
    public List<Expense> filterExpense(@RequestParam(name = "categoryId") Integer categoryId, Principal principal){
        return expenseService.filter(categoryId, principal);
    }

}
