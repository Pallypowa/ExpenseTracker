package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.AccountDTO;
import com.pico.budgetapplication.model.Account;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public AccountService(AccountRepository accountRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }


    public AccountDTO createAccount(AccountDTO accountDTO, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Account account = modelMapper.map(accountDTO, Account.class);
        account.setUser(user);
        accountRepository.save(account);
        return modelMapper.map(account, AccountDTO.class);
    }

    public List<AccountDTO> getMyAccounts(Principal principal){
        return new ArrayList<>();
    }

    public void updateAccount(){
        //TODO finish
    }

    public void deleteAccount(){
        //TODO finish
    }


    //AccountDTO should also be added
    public void deductExpense(Integer amount, User user){
        //TODO finish
    }

    public void addIncome(Integer amount, User user){
        //TODO finish
    }
}
