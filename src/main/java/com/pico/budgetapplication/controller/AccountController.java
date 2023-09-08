package com.pico.budgetapplication.controller;

import com.pico.budgetapplication.dto.AccountDTO;
import com.pico.budgetapplication.service.AccountService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok("Created");
    }
    @GetMapping("/findMyAccounts")
    public ResponseEntity<?> findMyAccounts(Principal principal){
        return new ResponseEntity<>(accountService.getMyAccounts(principal), HttpStatus.OK);
    }
    @PutMapping("/updateAccount")
    public ResponseEntity<?> findMyAccounts(@NotBlank @RequestBody AccountDTO accountDTO, Principal principal){
        accountService.updateAccount(accountDTO, principal);
        return ResponseEntity.ok("Updated");
    }
    @DeleteMapping("/deleteAccount")
    public ResponseEntity<?> deleteAccount(@NotBlank @RequestParam Long id, Principal principal){
        accountService.deleteAccount(id, principal);
        return ResponseEntity.ok("Deleted");
    }
}
