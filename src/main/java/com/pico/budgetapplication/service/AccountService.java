package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.AccountDTO;
import com.pico.budgetapplication.model.Account;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        List<Account> accounts = accountRepository.findAllByUser(user);
        return accounts
                .stream()
                .map(account
                        -> modelMapper.map(account, AccountDTO.class))
                .toList();
    }

    public void updateAccount(AccountDTO accountDTO, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Optional<Account> account = accountRepository.findById(accountDTO.getId().intValue());
        if(account.isEmpty() || !user.getId().equals(account.get().getUserId())){
            throw new RuntimeException("Record does not exist or you are not authorized to change it!");
        }
        Account newAccount = modelMapper.map(accountDTO, Account.class);
        newAccount.setUser(user);
        accountRepository.save(newAccount);
    }

    public void deleteAccount(Long id, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Optional<Account> account = accountRepository.findById(id.intValue());
        if(account.isEmpty()){
            throw new RuntimeException("User does not exist!");
        }
        if(!account.get().getUser().getId().equals(user.getId())){
            throw new AuthorizationServiceException("You are not authorized to change that account!");
        }
        accountRepository.delete(account.get());
    }



    //AccountDTO should also be added
    public void deductExpense(Long accountId, Integer amount, User user){
        //1. Get account
        Account account = accountRepository.findById(accountId.intValue()).orElseThrow();
        //2. Check if account belongs to the current user
        if(!isUserAccount(account, user)){
            throw new AuthorizationServiceException("You are not authorized to do that!");
        }
        //3. Subtract the expense
        account.setBalance(account.getBalance() - amount);
        //4. Save account
        accountRepository.save(account);
    }

    public void addIncome(Long accountId, Integer amount, User user){
        //1. Get account
        Account account = accountRepository.findById(accountId.intValue()).orElseThrow();
        //2. Check if account belongs to the current user
        if(!isUserAccount(account, user)){
            throw new AuthorizationServiceException("You are not authorized to do that!");
        }
        //3. Subtract the expense
        account.setBalance(account.getBalance() + amount);
        //4. Save account
        accountRepository.save(account);
    }

    private boolean isUserAccount(Account account, User user){
        return account.getUserId().equals(user.getId());
    }
}
