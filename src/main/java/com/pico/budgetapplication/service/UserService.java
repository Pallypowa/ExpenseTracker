package com.pico.budgetapplication.service;

import com.pico.budgetapplication.jwt.JwtService;
import com.pico.budgetapplication.model.ProjectUserDetails;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.UserRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String authenticate(String username, String password){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtService.generateToken(userDetails);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void register(User user){
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        //User already exists with the given user...
        if(foundUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        foundUser = userRepository.findByEmail(user.getEmail());
        //User already exists with the given email...
        if(foundUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        User newUser = new User(user.getUsername(), bCryptPasswordEncoder.encode(user.getPassword()), user.getEmail());
        try{
            userRepository.save(newUser);;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public ProjectUserDetails findMyUserDetails(Principal principal){
        User user =  ServiceUtil.getUserInstanceByPrincipal(principal);
        return userRepository.findAllById(user.getId()).get();
    }
}
