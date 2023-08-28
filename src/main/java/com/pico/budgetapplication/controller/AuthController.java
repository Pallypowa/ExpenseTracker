package com.pico.budgetapplication.controller;

import com.pico.budgetapplication.dto.RegistrationDTO;
import com.pico.budgetapplication.dto.AuthRequestDTO;
import com.pico.budgetapplication.model.AuthResponse;
import com.pico.budgetapplication.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequestDTO authRequestDTO){
        try{
            String jwt = userService.authenticate(authRequestDTO.getUsername(), authRequestDTO.getPassword());
            return new ResponseEntity<>(new AuthResponse(jwt), HttpStatus.OK);
        }catch(RuntimeException rte){
            return new ResponseEntity<>(rte.getMessage(),HttpStatus.OK);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@NotNull @RequestBody RegistrationDTO user){
        try {
            userService.register(user);
        }catch(ResponseStatusException rse){
            return new ResponseEntity<>(rse.getReason(), rse.getStatusCode());
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
