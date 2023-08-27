package com.pico.budgetapplication.controller;

import com.pico.budgetapplication.jwt.JwtService;
import com.pico.budgetapplication.model.AuthRequest;
import com.pico.budgetapplication.model.AuthResponse;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.UserRepository;
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

import java.util.Optional;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtService jwtService,
                          UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            String jwt = jwtService.generateToken(userDetails);
            return new ResponseEntity<>( new AuthResponse(jwt), HttpStatus.OK);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        if(user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        //User already exists with the given user...
        if(foundUser.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        foundUser = userRepository.findByEmail(user.getEmail());

        //User already exists with the given email...
        if(foundUser.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User newUser = new User(user.getUsername(), bCryptPasswordEncoder.encode(user.getPassword()), user.getEmail());

        try{
            userRepository.save(newUser);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
