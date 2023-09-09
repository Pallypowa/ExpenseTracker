package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.AccountDTO;
import com.pico.budgetapplication.dto.IncomeDTO;
import com.pico.budgetapplication.model.Account;
import com.pico.budgetapplication.model.Category;
import com.pico.budgetapplication.model.Income;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.CategoryRepository;
import com.pico.budgetapplication.repository.IncomeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final AccountService accountService;

    public IncomeService(IncomeRepository incomeRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, AccountService accountService) {
        this.incomeRepository = incomeRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.accountService = accountService;
    }

    public IncomeDTO save(Account account, IncomeDTO incomeDTO, User user){
        Income income = modelMapper.map(incomeDTO, Income.class);
        income.setUser(user);
        income.setAccount(account);
        Category category = ServiceUtil.getCategoryForObject(incomeDTO.getCategoryName(), user, categoryRepository);
        income.setCategory(category);
        return modelMapper.map(incomeRepository.save(income), IncomeDTO.class);
    }

    public List<IncomeDTO> findUserIncomes(Principal principal) {
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        return incomeRepository.findAllByUser(user)
                .stream()
                .map(income -> modelMapper.map(income, IncomeDTO.class))
                .toList();
    }

    public void delete(UUID id, Principal principal) {
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Income income = incomeRepository.findById(id).orElseThrow();
        if(!income.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You are not authorized to do that!");
        }
        incomeRepository.delete(income);
    }

    public IncomeDTO update(IncomeDTO incomeDTO, Principal principal) {
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Income incomeFromDb = incomeRepository.findById(incomeDTO.getId()).orElseThrow();
        if(!incomeFromDb.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You are not authorized to do that! (update another user's expense)");
        }
        List<AccountDTO> userAccounts = accountService.getMyAccounts(principal);
        if(userAccounts.stream().noneMatch(accountDTO -> accountDTO.getId().equals(incomeDTO.getAccountId()))){
            throw new RuntimeException("You are not authorized to do that! (update to another user's account) ");
        }
        Income income = modelMapper.map(incomeDTO, Income.class);
        income.setCategory(incomeFromDb.getCategory());
        income.setUser(user);
        return modelMapper.map(incomeRepository.save(income), IncomeDTO.class);
    }
}
