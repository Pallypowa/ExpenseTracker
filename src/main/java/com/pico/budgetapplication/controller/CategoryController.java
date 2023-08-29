package com.pico.budgetapplication.controller;


import com.pico.budgetapplication.dto.CategoryDTO;
import com.pico.budgetapplication.dto.CategoryUpdateDTO;
import com.pico.budgetapplication.model.Category;
import com.pico.budgetapplication.service.CategoryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/expense_tracker/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/findMyCategories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDTO> findAll(Principal principal){
        return categoryService.findMyCategories(principal);
    }

    @PostMapping("/createCategory")
    public ResponseEntity<?> createCategory(@NotBlank @RequestBody CategoryDTO category, Principal principal){
        try{
            categoryService.createCategory(category, principal);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(ResponseStatusException e){
            return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        }
    }

    @PutMapping("/updateCategory")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateCategory(@NotNull @RequestBody CategoryUpdateDTO category, Principal principal){
        try{
            categoryService.updateCategory(category, principal);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteCategory")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteCategory(@NotBlank @RequestParam(name = "categoryName") String name, Principal principal){
        try{
            categoryService.deleteCategory(name,principal);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(ResponseStatusException rte){
            return new ResponseEntity<>(rte.getMessage(), rte.getStatusCode());
        }

    }

}
