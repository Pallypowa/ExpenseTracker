package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.RegistrationDTO;
import com.pico.budgetapplication.dto.UserDTO;
import com.pico.budgetapplication.jwt.JwtService;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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
    private final ModelMapper modelMapper;
    private final AccountService accountService;

    public UserService(AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       JwtService jwtService,
                       UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       ModelMapper modelMapper, AccountService accountService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.accountService = accountService;
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

    public void register(RegistrationDTO user){
        Optional<User> foundUser = userRepository.findByUsername(user.username());
        //User already exists with the given user...
        if(foundUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        foundUser = userRepository.findByEmail(user.email());
        //User already exists with the given email...
        if(foundUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        User newUser = new User(user.username(),
                bCryptPasswordEncoder.encode(user.password()),
                user.email(),
                user.firstName(),
                user.lastName(),
                user.age(),
                user.gender());
        try{
            newUser = userRepository.save(newUser);
            accountService.createAccountForNewUser(user.currency(), newUser);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public UserDTO findMyUserDetails(Principal principal){
        User user =  ServiceUtil.getUserInstanceByPrincipal(principal);
        User foundUser = userRepository.findById(user.getId()).get();
        return modelMapper.map(foundUser, UserDTO.class);
    }
}
