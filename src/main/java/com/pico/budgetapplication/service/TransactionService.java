package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.ExpenseDTO;
import com.pico.budgetapplication.dto.IncomeDTO;
import com.pico.budgetapplication.dto.SavingDTO;
import com.pico.budgetapplication.dto.SavingTransDTO;
import com.pico.budgetapplication.model.Account;
import com.pico.budgetapplication.model.Saving;
import com.pico.budgetapplication.model.SavingTransHist;
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
    private final SavingService savingService;

    public TransactionService(ExpenseService expenseService,
                              AccountService accountService,
                              IncomeService incomeService, SavingService savingService) {
        this.accountService = accountService;
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.savingService = savingService;
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
    public List<SavingDTO> getMySavings(Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        return savingService.getMySavings(user.getId());
    }
    public SavingDTO addSaving(SavingDTO savingDTO, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        return savingService.addSaving(savingDTO, user);
    }
    public SavingTransDTO addTransaction(UUID accountId, SavingTransDTO transaction, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        return savingService.addTransaction(accountId,transaction,user.getId());
    }

    public SavingDTO updateSaving(SavingDTO savingDTO, Principal principal) {
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        return savingService.updateSaving(savingDTO, user);
    }

    public void deleteSaving(UUID savingId, Principal principal) {
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Saving saving = savingService.deleteSaving(savingId, user);
        Integer balanceToUpdate = saving.getCurrentAmount();

        Account mainAccount = accountService.getMainAccountForUser(user.getId());
        Integer mainAccountBalance = mainAccount.getBalance();
        mainAccount.setBalance(mainAccountBalance + balanceToUpdate);
        accountService.updateAccount(mainAccount);
    }

    public SavingTransDTO updateTransactionHistory(SavingTransDTO transaction, Principal principal) {
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);

        SavingTransHist savingTransHist = savingService.getSavingTransHist(transaction.getId());
        //We add this difference to the account balance
        Integer difference = savingTransHist.getAmount() - transaction.getAmount();

        Saving savingToUpdate = savingService.getSavingById(transaction.getSavingId());
        savingToUpdate.setCurrentAmount(savingToUpdate.getCurrentAmount() + difference * -1);
        savingService.updateSaving(savingToUpdate, user);

        Account accountToUpdate = accountService.getMainAccountForUser(user.getId());
        accountToUpdate.setBalance(accountToUpdate.getBalance() + difference);
        accountService.updateAccount(accountToUpdate);

        return savingService.updateTransactionHist(transaction, user);
    }

    public void deleteSavingTransHist(UUID savingTransHistId, Principal principal) {
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Account mainAccount = accountService.getMainAccountForUser(user.getId());
        SavingTransHist savingTransHist = savingService.getSavingTransHist(savingTransHistId);
        mainAccount.setBalance(mainAccount.getBalance() + savingTransHist.getAmount());
        accountService.updateAccount(mainAccount);

        Saving saving = savingService.getSavingById(savingTransHist.getSaving().getId());
        saving.setCurrentAmount(saving.getCurrentAmount() - savingTransHist.getAmount());
        savingService.updateSaving(saving, user);

        savingService.deleteSavingTransHist(savingTransHist, user);
    }
}
