package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.ExpenseDTO;
import com.pico.budgetapplication.model.Category;
import com.pico.budgetapplication.model.Expense;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.CategoryRepository;
import com.pico.budgetapplication.repository.ExpenseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public List<ExpenseDTO> findUserExpenses(Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        return expenseRepository.findAllByUser(user).stream().map(
                expense -> modelMapper.map(expense, ExpenseDTO.class)
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

        return modelMapper.map(optionalExpense, ExpenseDTO.class);
    }

    public void save(ExpenseDTO expenseDTO, User user){
        //Create expense for the authenticated user
        Expense expense = modelMapper.map(expenseDTO, Expense.class);
        expense.setUser(new User(user.getId()));

        setCategoryForExpense(expenseDTO, user, expense);
        expenseRepository.save(expense);
    }

    private void setCategoryForExpense(ExpenseDTO expenseDTO, User user, Expense expense) {
        Optional<List<Category>> category = categoryRepository.findCatByName(expenseDTO.getCategoryName());

        if(category.get().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist!");
        }

        Optional<Category> cat =  category
                .get()
                .stream()
                .filter(c -> c.userIsNull()
                ).findFirst();

        if(cat.isEmpty()){
            cat = category
                    .get()
                    .stream()
                    .filter(c -> c.getUser().getId().equals(user.getId())
                    ).findFirst();
        }
        cat.ifPresent(expense::setCategory);
    }

    public void saveAllExpenses(List<ExpenseDTO> expenseDTOList, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        //Expenses should be created for the authenticated user
        List<Expense> expenseList = expenseDTOList.stream().map( expenseDTO
                -> {
            Expense expense = modelMapper.map(expenseDTO, Expense.class);
            setCategoryForExpense(expenseDTO, user, expense);

            expense.setUser(new User(user.getId()));
            return expense;
        }).collect(Collectors.toList());
        expenseRepository.saveAll(expenseList);
    }

    public ExpenseDTO update(ExpenseDTO expenseDTO, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseDTO.getId().intValue());
        if(optionalExpense.isEmpty()){
            return null;
        }
        Expense expense = modelMapper.map(expenseDTO, Expense.class);
        expense.setUser(user);
        expense.setCategory(optionalExpense.get().getCategory());
        expense.setPaymentMethod(expenseDTO.getPaymentMethod());
        return modelMapper.map(expenseRepository.save(expense), ExpenseDTO.class);
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

    //TODO finish
    public List<ExpenseDTO> filter(LocalDate startDate,
                                   LocalDate endDate,
                                   Map<String, String> requestParams,
                                   Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);

        final String category = "category";
        final String minAmount = "minAmount";
        final String maxAmount = "maxAmount";

        //Check if there is any valid parameter...
        List<Expense> expenses = expenseRepository.findAllByDateBetween(
                LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                LocalDateTime.of(endDate, LocalTime.MIDNIGHT));

        expenses.removeIf( expense -> !expense.getUserId().equals(user.getId()));


        if(requestParams.keySet().stream().anyMatch(key -> key.equals(category) || key.equals(minAmount) || key.equals(maxAmount))){
            requestParams.forEach((key,value)->{
                switch (key){
                    case category ->
                            expenses.removeIf( expense -> !expense.getCategory().getCategoryName().equals(value));
                    case minAmount ->
                            expenses.removeIf( expense -> expense.getAmount() < Integer.parseInt(value));
                    case maxAmount ->
                            expenses.removeIf( expense -> expense.getAmount() > Integer.parseInt(value));
                }

            });
        }

        return expenses.stream().map(
                e -> modelMapper.map(e, ExpenseDTO.class)
                ).toList();
    }
}
