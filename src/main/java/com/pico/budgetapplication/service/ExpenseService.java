package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.ExpenseDTO;
import com.pico.budgetapplication.model.Category;
import com.pico.budgetapplication.model.Expense;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.CategoryRepository;
import com.pico.budgetapplication.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ExpenseDTO> findUserExpenses(Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        return expenseRepository.findAllByUser(user).stream().map(
                expense -> new ExpenseDTO(expense.getId(), expense.getAmount(),
                        expense.getDate(),
                        expense.getCurrency(),
                        expense.getDesc(),
                        expense.getCategory().getCategoryName(),
                        expense.getPaymentMethod())
        ).collect(Collectors.toList());
    }

    public ExpenseDTO findById(Integer id, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User's principal not found");
        }
        Optional<Expense> fetchedExpense = expenseRepository.findById(id);
        if(fetchedExpense.isEmpty() || fetchedExpense.get().getUserId().intValue() != user.getId()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Expense does not exist or user does not exist for this expense.");
        }

        Expense optionalExpense = expenseRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found") );

        return new ExpenseDTO(
                optionalExpense.getId(),
                optionalExpense.getAmount(),
                optionalExpense.getDate(),
                optionalExpense.getCurrency(),
                optionalExpense.getDesc(),
                optionalExpense.getCategory().getCategoryName(),
                optionalExpense.getPaymentMethod()
                );
    }

    public void save(ExpenseDTO expenseDTO, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        //Create expense for the authenticated user
        Expense expense = new Expense(expenseDTO.amount(), expenseDTO.addedAt(), expenseDTO.currency(), expenseDTO.desc(), null, null, expenseDTO.paymentMethod());
        expense.setUser(new User(user.getId()));
        expense.setCategory(new Category(expenseDTO.categoryName()));

        expenseRepository.save(expense);
    }

    public void saveAllExpenses(List<ExpenseDTO> expenseDTOList, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        //Expenses should be created for the authenticated user
        List<Expense> expenseList = expenseDTOList.stream().map( expenseDTO
                -> {
            Expense expense = new Expense(expenseDTO.amount(), expenseDTO.addedAt(), expenseDTO.currency(), expenseDTO.desc());
            expense.setUser(new User(user.getId()));
            return expense;
        }).collect(Collectors.toList());

        expenseRepository.saveAll(expenseList);
    }

    public Expense update(ExpenseDTO expenseDTO, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseDTO.id().intValue());
        if(optionalExpense.isEmpty()){
            return null;
        }
        Expense expense = new Expense(expenseDTO.id(),expenseDTO.amount(), expenseDTO.addedAt(), expenseDTO.currency(), expenseDTO.desc());
        expense.setUser(user);
        expense.setCategory(optionalExpense.get().getCategory());
        expense.setPaymentMethod(expenseDTO.paymentMethod());
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

    public List<ExpenseDTO> filter(Integer categoryId, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        List<ExpenseDTO> expenseByCategory = expenseRepository.findByCategory(categoryId).stream()
                .filter( e-> Objects.equals(e.getUserId(), user.getId()))
                .map(expense ->
                    new ExpenseDTO(
                            expense.getId(),
                            expense.getAmount(),
                            expense.getDate(),
                            expense.getCurrency(),
                            expense.getDesc(),
                            expense.getCategory().getCategoryName(),
                            expense.getPaymentMethod())
                ).collect(Collectors.toList());
        if(!expenseByCategory.isEmpty()){
            return expenseByCategory;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No expense with the given category!");
    }
}
