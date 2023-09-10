package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.ExpenseDTO;
import com.pico.budgetapplication.dto.IncomeDTO;
import com.pico.budgetapplication.model.*;
import com.pico.budgetapplication.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public class ServiceUtil {
    public static User getUserInstanceByPrincipal(Principal principal){
        UsernamePasswordAuthenticationToken userNamePwAuthToken = (UsernamePasswordAuthenticationToken) principal;
        return (User) userNamePwAuthToken.getPrincipal();
    }

    public static Category getCategoryForObject(String categoryName, User user, CategoryRepository categoryRepository) {
        List<Category> category = categoryRepository.findCatByName(categoryName).orElseThrow();

        if(category.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist!");
        }

        Optional<Category> cat =  category
                .stream()
                .filter(c -> c.userIsNull()
                ).findFirst();

        if(cat.isEmpty()){
            cat = category
                    .stream()
                    .filter(c -> c.getUser().getId().equals(user.getId())
                    ).findFirst();
        }
        return cat.get();
    }

    public static boolean isUserAccount(Account account, Long userId){
        return account.getUserId().equals(userId);
    }

}
