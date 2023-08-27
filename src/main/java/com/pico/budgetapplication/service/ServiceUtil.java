package com.pico.budgetapplication.service;

import com.pico.budgetapplication.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

public class ServiceUtil {
    public static User getUserInstanceByPrincipal(Principal principal){
        UsernamePasswordAuthenticationToken userNamePwAuthToken = (UsernamePasswordAuthenticationToken) principal;
        return (User) userNamePwAuthToken.getPrincipal();
    }
}
