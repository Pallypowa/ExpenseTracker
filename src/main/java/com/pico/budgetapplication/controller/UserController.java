package com.pico.budgetapplication.controller;

import com.pico.budgetapplication.dto.UserDTO;
import com.pico.budgetapplication.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/expense_tracker/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/myUserDetails")
    public UserDTO getMyUserName(Principal principal){
        return userService.findMyUserDetails(principal);
    }

}
