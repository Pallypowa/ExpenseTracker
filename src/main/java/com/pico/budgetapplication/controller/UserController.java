package com.pico.budgetapplication.controller;

import com.pico.budgetapplication.model.ProjectUserDetails;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.UserRepository;
import com.pico.budgetapplication.service.ServiceUtil;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/expense_tracker/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/findAll")
//    public List<User> findAll(){
//        return userRepository.findAll();
//    }

//    @PostMapping(value = "/createUser", consumes = {"application/json;charset=UTF-8"})
//    public ResponseEntity<?> createUser(@RequestBody User user){
//
//        userRepository.save(user);
//        return new ResponseEntity<>(user, HttpStatus.CREATED);
//    }

    @GetMapping("/myUserDetails")
    public ProjectUserDetails getMyUserName(Principal principal){
        User user =  ServiceUtil.getUserInstanceByPrincipal(principal);
        return userRepository.findAllById(user.getId()).get();
    }

}
