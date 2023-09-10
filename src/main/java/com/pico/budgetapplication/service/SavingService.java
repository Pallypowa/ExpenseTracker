package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.SavingDTO;
import com.pico.budgetapplication.dto.SavingTransDTO;
import com.pico.budgetapplication.model.*;
import com.pico.budgetapplication.repository.SavingRepository;
import com.pico.budgetapplication.repository.SavingTransHistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SavingService {
    private final SavingRepository savingRepository;
    private final SavingTransHistRepository savingTransHistRepository;
    private final AccountService accountService;

    private final ModelMapper modelMapper;

    public SavingService(SavingRepository savingRepository,
                         SavingTransHistRepository savingTransHistRepository,
                         AccountService accountService, ModelMapper modelMapper) {
        this.savingRepository = savingRepository;
        this.savingTransHistRepository = savingTransHistRepository;
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    public SavingDTO addSaving(SavingDTO savingDTO, User user){
        Saving saving = modelMapper.map(savingDTO, Saving.class);
        saving.setUser(user);
        saving.setCurrentAmount(0);
        return modelMapper.map(savingRepository.save(saving), SavingDTO.class);
    }

    public List<SavingDTO> getMySavings(Long userId){
        return savingRepository
                .findAllByUserId(userId)
                .stream()
                .map(saving -> {
                    SavingDTO mappedRecord = modelMapper.map(saving, SavingDTO.class);
                    mappedRecord.setFinished(mappedRecord.getCurrentAmount() >= mappedRecord.getGoal());
                    return mappedRecord;
                }).toList();
    }

    public SavingTransDTO addTransaction(UUID accountId, SavingTransDTO transaction, Long userId){
        //Check if given saving exist
        Saving saving = savingRepository.findById(transaction.getSavingId()).orElseThrow(() -> new RuntimeException("Saving does not exist!"));
        //Check if the saving belongs to the user
        if(!saving.getUser().getId().equals(userId)){
            throw new RuntimeException("You are not authorized to do that!");
        }
        //Deduct saving amount from account
        if(transaction.getTransactionType().equals(SavingTransType.WITHDRAW) && transaction.getAmount() > saving.getCurrentAmount()){
            throw new RuntimeException("You can't withdraw that amount of money!");
        }
        updateAccountBalance(accountId, transaction, userId);
        updateSavingAmount(saving, transaction);
        SavingTransHist savingTransHist = modelMapper.map(transaction, SavingTransHist.class);
        return modelMapper.map(savingTransHistRepository.save(savingTransHist), SavingTransDTO.class);
    }



    private void updateSavingAmount(Saving saving, SavingTransDTO transaction) {
        Integer currentAmount = saving.getCurrentAmount();
        if(transaction.getTransactionType().equals(SavingTransType.DEPOSIT)){
            currentAmount += transaction.getAmount();
        } else if (transaction.getTransactionType().equals(SavingTransType.WITHDRAW)) {
            currentAmount -= transaction.getAmount();
        }
        saving.setCurrentAmount(currentAmount);
        savingRepository.save(saving);
    }

    private void updateAccountBalance(UUID accountId, SavingTransDTO transaction, Long userid){
        Account account = accountService.findAccountById(accountId);
        if(!ServiceUtil.isUserAccount(account, userid)){
            throw new RuntimeException("You are not authorized to do that!");
        }
        Integer currentBalance = account.getBalance();
        //Subtract/Add the transaction from the given account
        if(transaction.getTransactionType().equals(SavingTransType.DEPOSIT)){
            account.setBalance(currentBalance - transaction.getAmount());
        } else if (transaction.getTransactionType().equals(SavingTransType.WITHDRAW)) {
            account.setBalance(currentBalance + transaction.getAmount());
        }
        accountService.updateAccount(account);
    }

    public SavingDTO updateSaving(SavingDTO savingDTO, User user) {
        Saving saving = savingRepository.findById(savingDTO.getId()).orElseThrow();
        if(!saving.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You can't do that!");
        }
        saving = modelMapper.map(savingDTO, Saving.class);
        saving.setUser(user);
        return modelMapper.map(savingRepository.save(saving), SavingDTO.class);
    }

    public void updateSaving(Saving saving, User user) {
        if(!saving.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You can't do that!");
        }
        savingRepository.save(saving);
    }

    public Saving deleteSaving(UUID savingId, User user) {
        Saving saving = savingRepository.findById(savingId).orElseThrow();
        if(!saving.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You can't do that!");
        }
        savingRepository.delete(saving);
        return saving;
    }

    public SavingTransHist getSavingTransHist(UUID id){
        return savingTransHistRepository.findById(id).orElseThrow();
    }

    public Saving getSavingById(UUID id){
        return savingRepository.findById(id).orElseThrow();
    }

    public SavingTransDTO updateTransactionHist(SavingTransDTO transaction, User user) {
        SavingTransHist savingTransHist = savingTransHistRepository.findById(transaction.getId()).orElseThrow();
        if(!savingTransHist.getSaving().getUser().getId().equals(user.getId())){
            throw new RuntimeException("You can't do that!");
        }
        savingTransHist = savingTransHistRepository.save(modelMapper.map(transaction, SavingTransHist.class));
        return modelMapper.map(savingTransHist, SavingTransDTO.class);
    }

    public void deleteSavingTransHist(SavingTransHist savingTransHist, User user) {
        if(!savingTransHist.getSaving().getUser().getId().equals(user.getId())){
            throw new RuntimeException("You can't do that!");
        }

        savingTransHistRepository.delete(savingTransHist);
    }
}
