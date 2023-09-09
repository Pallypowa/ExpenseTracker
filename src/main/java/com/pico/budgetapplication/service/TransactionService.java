package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.ExpenseDTO;
import com.pico.budgetapplication.dto.IncomeDTO;
import com.pico.budgetapplication.model.Account;
import com.pico.budgetapplication.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        //1. Subtract value from user account
        Account account = accountService.addTransaction(expenseDTO.getAccountId(), expenseDTO.getAmount(), user, Transaction.EXPENSE);
        //2. Create Expense record for user
        return expenseService.save(account, expenseDTO, user);
    }

    public IncomeDTO addIncome(IncomeDTO incomeDTO, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        //1. Add value to user account
        Account account = accountService.addTransaction(incomeDTO.getAccountId(),incomeDTO.getAmount(),user,Transaction.INCOME);
        //2. Create income record for user
        return incomeService.save(account, incomeDTO, user);
    }

    public List<IncomeDTO> findMyIncomes(Principal principal) {
        return incomeService.findUserIncomes(principal);
    }

    public List<ExpenseDTO> findMyExpenses(Principal principal) {
        return expenseService.findUserExpenses(principal);
    }

    public IncomeDTO updateIncome(IncomeDTO incomeDTO, Principal principal) {
        return incomeService.update(incomeDTO, principal);

    }

    public ExpenseDTO updateExpense(ExpenseDTO expenseDTO, Principal principal) {
        return expenseService.update(expenseDTO, principal);
    }

    public void deleteIncome(@NotNull UUID id, Principal principal) {
        incomeService.delete(id, principal);
    }

    public void deleteExpense(@NotNull UUID id, Principal principal) {
        expenseService.delete(id, principal);
    }
    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate,
                                           Map<String, String> requestParams, Principal principal){
        return expenseService.filter(startDate,endDate,requestParams,principal);
    }
}
