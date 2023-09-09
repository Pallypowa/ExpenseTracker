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
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final String MAIN_ACCOUNT = "Main";

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

    public void createAccountForNewUser(String currency, User user){
        Account account = new Account();
        account.setUser(user);
        account.setBalance(0);
        account.setCurrency(currency);
        account.setAccountName(MAIN_ACCOUNT);
        accountRepository.save(account);
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
        Optional<Account> account = accountRepository.findById(accountDTO.getId());
        if(account.isEmpty() || !user.getId().equals(account.get().getUserId())){
            throw new RuntimeException("Record does not exist or you are not authorized to change it!");
        }
        Account newAccount = modelMapper.map(accountDTO, Account.class);
        newAccount.setUser(user);
        accountRepository.save(newAccount);
    }

    public void deleteAccount(UUID id, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Optional<Account> account = accountRepository.findById(id);
        if(account.isEmpty()){
            throw new RuntimeException("User does not exist!");
        }
        if(!account.get().getUser().getId().equals(user.getId())){
            throw new AuthorizationServiceException("You are not authorized to change that account!");
        }
        accountRepository.delete(account.get());
    }

    public Account addTransaction(UUID accountId, Integer amount, User user, Transaction transaction){
        //1. Get account
        Account account = accountRepository.findById(accountId).orElseThrow();
        //2. Check if account belongs to the current user
        if(!isUserAccount(account, user)){
            throw new AuthorizationServiceException("You are not authorized to do that!");
        }
        //3. Income...
        if(transaction == Transaction.INCOME){
            account.setBalance(account.getBalance() + amount);
        //Expense...
        } else {
            account.setBalance(account.getBalance() - amount);
        }
        //4. Save account
        return accountRepository.save(account);
    }

    private boolean isUserAccount(Account account, User user){
        return account.getUserId().equals(user.getId());
    }
}
