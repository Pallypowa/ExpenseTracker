package com.pico.budgetapplication.service;

import com.pico.budgetapplication.model.Category;
import com.pico.budgetapplication.model.User;
import com.pico.budgetapplication.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void createCategory(Category category, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Category newCategory = new Category(category.getCategoryName(), user.getId());
        categoryRepository.save(newCategory);
    }

    public List<Category> findMyCategories(Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        List<Category> categoryList = categoryRepository.findAllByUserId(user.getId());
        categoryList.addAll(categoryRepository.findAllByUserId(null));
        return categoryList;
    }


    public void deleteCategory(String name, Principal principal) {
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Optional<Category> category = categoryRepository.findByCategoryName(name);

        if(category.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist");
        }

        if(category.get().userIsNull()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't delete default categories");
        }

        if(!Objects.equals(category.get().getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only delete your user's categories");
        }
        categoryRepository.deleteByCategoryName(name);
    }

    public void updateCategory(Category category, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);

        Optional<Category> categoryInDb = categoryRepository.findById(category.getId());

        if(categoryInDb.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist");
        }

        if(categoryInDb.get().userIsNull()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't edit default categories");
        }

        if(!Objects.equals(categoryInDb.get().getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only delete your user's categories");
        }

        categoryRepository.save(category);

    }
}
