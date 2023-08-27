package com.pico.budgetapplication.controller;


import com.pico.budgetapplication.model.Category;
import com.pico.budgetapplication.repository.CategoryRepository;
import com.pico.budgetapplication.service.CategoryService;
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

    public CategoryController(CategoryRepository categoryRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @GetMapping("/findMyCategories")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> findAll(Principal principal){
        return categoryService.findMyCategories(principal);
    }

    @PostMapping("/createCategory")
    public ResponseEntity<?> createCategory(@NotNull @RequestBody Category category, Principal principal){
        try{
            categoryService.createCategory(category, principal);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateCategory")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateCategory(@NotNull @RequestBody Category category, Principal principal){
        try{
            categoryService.updateCategory(category, principal);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteCategory")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteCategory(@NotNull @RequestParam(name = "categoryName") String name, Principal principal){
        try{
            categoryService.deleteCategory(name,principal);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(ResponseStatusException rte){
            return new ResponseEntity<>(rte.getMessage(), rte.getStatusCode());
        }

    }

}
