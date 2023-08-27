package com.pico.budgetapplication.controller;

import com.pico.budgetapplication.jwt.JwtService;
import com.pico.budgetapplication.model.AuthRequest;
import com.pico.budgetapplication.model.AuthResponse;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.UserRepository;
import com.pico.budgetapplication.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest){
        try{
            String jwt = userService.authenticate(authRequest.getUsername(), authRequest.getPassword());
            return new ResponseEntity<>(new AuthResponse(jwt), HttpStatus.OK);
        }catch(RuntimeException rte){
            return new ResponseEntity<>(rte.getMessage(),HttpStatus.OK);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@NotNull @RequestBody User user){
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
