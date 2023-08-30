package com.pico.budgetapplication.controller;


import com.pico.budgetapplication.dto.ExpenseDTO;
import com.pico.budgetapplication.model.Expense;
import com.pico.budgetapplication.service.ExpenseService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


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
    public List<ExpenseDTO> findMyExpenses(Principal principal){
        return expenseService.findUserExpenses(principal);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findById")
    public ExpenseDTO findById(@RequestParam(name = "id") Integer id, Principal principal){
        return expenseService.findById(id, principal);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/createExpense")
    public ResponseEntity<?> saveExpense(@RequestBody ExpenseDTO expense, Principal principal){
        expenseService.save(expense, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/createExpensesList")
    public ResponseEntity<?> saveAllExpenses(@RequestBody List<ExpenseDTO> expenseList, Principal principal){
        expenseService.saveAllExpenses(expenseList, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/updateExpense")
    public ResponseEntity<?> updateExpense(@RequestBody ExpenseDTO expenseDTO, Principal principal){
        Expense expense = expenseService.update(expenseDTO, principal);
        if(expense != null)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/deleteExpense")
    public void deleteExpense(@RequestParam(name = "id") Integer id, Principal principal){
        expenseService.delete(id, principal);
    }

//    @RequestParam(name = "categoryId", required = false) Integer categoryId,
//    @RequestParam(value = "startDate", required = false) LocalDateTime startInterval,
//    @RequestParam(value = "endDate", required = false) LocalDateTime endInterval,
//    @RequestParam(value = "minAmount", required = false) Integer minAmount,
//    @RequestParam(value = "maxAmount", required = false) Integer maxAmount,

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filter")
    public List<ExpenseDTO> filterExpenses(
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate,
            @RequestParam Map<String, String> requestParams, Principal principal){
        return expenseService.filter(startDate, endDate, requestParams, principal);
    }
}
