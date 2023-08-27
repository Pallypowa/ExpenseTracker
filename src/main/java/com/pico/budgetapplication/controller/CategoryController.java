package com.pico.budgetapplication.controller;


import com.pico.budgetapplication.model.Category;
import com.pico.budgetapplication.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense_tracker/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/findAll")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    @PostMapping("/createCategory")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCategory(@RequestBody Category category){
        categoryRepository.save(category);

        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/updateCategory")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateCategory(@RequestBody Category category){
        categoryRepository.save(category);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/deleteCategory")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@RequestParam(name = "id") Integer id){
        categoryRepository.deleteById((long) id);
    }

}
