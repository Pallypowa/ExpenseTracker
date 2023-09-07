package com.pico.budgetapplication.controller;

import com.pico.budgetapplication.dto.AccountDTO;
import com.pico.budgetapplication.service.AccountService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/expense_tracker/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@NotBlank @RequestBody AccountDTO accountDTO, Principal principal){
        accountService.createAccount(accountDTO, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
