package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.ExpenseDTO;
import com.pico.budgetapplication.dto.IncomeDTO;
import com.pico.budgetapplication.model.User;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class TransactionService {
    private final AccountService accountService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public TransactionService(ExpenseService expenseService,
                              AccountService accountService,
                              IncomeService incomeService) {
        this.accountService = accountService;
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    public void addExpense(Long accountId, ExpenseDTO expenseDTO, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        //1. Subtract value from user account
        accountService.deductExpense(accountId, expenseDTO.getAmount(), user);
        //2. Create Expense record for user
        expenseService.save(expenseDTO, user);
    }

    public void addIncome(Long accountId, IncomeDTO incomeDTO, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        //1. Add value to user account
        accountService.addIncome(accountId,incomeDTO.getAmount(), user);
        //2. Create income record for user
        incomeService.save(incomeDTO, user);
    }
}
