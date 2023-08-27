package com.pico.budgetapplication.service;

import com.pico.budgetapplication.model.Expense;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.ExpenseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> findUserExpenses(Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        return expenseRepository.findAllByUser(user);
    }

    public Optional<Expense> findById(Integer id, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User's principal not found");
        }
        Optional<Expense> fetchedExpense = expenseRepository.findById(id);
        if(fetchedExpense.isEmpty() || fetchedExpense.get().getUserId().intValue() != user.getId()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Expense does not exist or user does not exist for this expense.");
        }

        return Optional.ofNullable(expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found")));
    }

    public Expense save(Expense expense, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        //Create expense for the authenticated user
        expense.setUser(new User(user.getId()));

        return expenseRepository.save(expense);
    }

    public List<Expense> saveAllExpenses(List<Expense> expenseList, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);

        //Expenses should be created for the authenticated user
        for(Expense expense : expenseList){
            expense.setUser(new User(user.getId()));
        }

        return expenseRepository.saveAll(expenseList);
    }

    public Expense update(Expense expense, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        expense.setUser(new User(user.getId()));
        return expenseRepository.save(expense);
    }

    public void delete(Integer id, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Optional<Expense> expense = expenseRepository.findById(id);
        if(expense.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expense does not exist");
        }
        if(expense.get().getUser().getId().intValue() != user.getId().intValue()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete other people's expenses!");
        }
        expenseRepository.deleteById(id);
    }

    public List<Expense> filter(Integer categoryId, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        List<Expense> expenseByCategory = expenseRepository.findByCategory(categoryId).stream()
                .filter( e-> Objects.equals(e.getUserId(), user.getId())).toList();
        if(!expenseByCategory.isEmpty()){
            return expenseByCategory;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No expense with the given category!");
    }
}
