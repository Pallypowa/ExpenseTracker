package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.IncomeDTO;
import com.pico.budgetapplication.model.Category;
import com.pico.budgetapplication.model.Income;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.CategoryRepository;
import com.pico.budgetapplication.repository.IncomeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public IncomeService(IncomeRepository incomeRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.incomeRepository = incomeRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public void save(IncomeDTO incomeDTO, User user){
        Income income = modelMapper.map(incomeDTO, Income.class);
        income.setUser(user);

        setCategoryForExpense(incomeDTO, user, income);
        incomeRepository.save(income);
    }

    private void setCategoryForExpense(IncomeDTO incomeDTO, User user, Income income) {
            Optional<List<Category>> category = categoryRepository.findCatByName(incomeDTO.getCategoryName());

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
            cat.ifPresent(income::setCategory);
    }
}
