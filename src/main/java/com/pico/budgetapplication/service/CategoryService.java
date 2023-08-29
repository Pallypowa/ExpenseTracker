package com.pico.budgetapplication.service;

import com.pico.budgetapplication.dto.CategoryDTO;
import com.pico.budgetapplication.dto.CategoryUpdateDTO;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void createCategory(CategoryDTO category, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Optional<List<Category>> categories = categoryRepository.findCatByName(category.categoryName());

        if(!categories.get().isEmpty()){
            if(categories.get().stream().anyMatch(c -> c.userIsNull())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A default category already exist");
            if(categories.get().stream().anyMatch(c -> c.getUser().getId().equals(user.getId()))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You already have this category");
        }
        Category newCategory = new Category(category.categoryName(), user.getId());
        categoryRepository.save(newCategory);
    }

    public List<CategoryDTO> findMyCategories(Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        List<Category> categoryList = categoryRepository.findAllByUserId(user.getId());
        categoryList.addAll(categoryRepository.findAllByUserId(null));

        return categoryList.stream().map(
                category -> new CategoryDTO(category.getCategoryName())
        ).collect(Collectors.toList());
    }


    public void deleteCategory(String categoryName, Principal principal) {
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Optional<List<Category>> categoryInDb = categoryRepository.findCatByName(categoryName);

        if(categoryInDb.get().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist");
        }

        if(categoryInDb.get().size() == 1){
            if(categoryInDb.get().get(0).userIsNull()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't delete default categories!");
            if(!Objects.equals(categoryInDb.get().get(0).getUser().getId(), user.getId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't delete other people's categories!");
            categoryRepository.delete(categoryInDb.get().get(0));
        }
        categoryInDb.get().removeIf( c -> c.userIsNull());
        if(categoryInDb.get().stream().noneMatch( c -> c.getUser().getId().equals(user.getId()))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't delete other people's categories!");
        Category category = categoryInDb.get().stream().filter( c -> c.getUser().getId().equals(user.getId())).findFirst().get();
        categoryRepository.delete(category);
    }

    public void updateCategory(CategoryUpdateDTO categoryUpdateDTO, Principal principal){
        User user = ServiceUtil.getUserInstanceByPrincipal(principal);
        Optional<List<Category>> categoryInDb = categoryRepository.findCatByName(categoryUpdateDTO.oldName());

        if(categoryInDb.get().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist");
        }

        if(categoryInDb.get().size() == 1){
            Category category = categoryInDb.get().get(0);
            if(category.userIsNull()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't edit default categories");
            if(!category.getUser().getId().equals(user.getId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't edit other people's categories!");
            category.setCategoryName(categoryUpdateDTO.newName());
            categoryRepository.save(category);
        }

        List<Category> categoryList = categoryInDb.get().stream().filter(c -> {
            if(c.userIsNull()){
                return false;
            }
            return c.getUser().getId().equals(user.getId());
        }).toList();

        if(categoryList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't edit other people's categories!");
        }

        Category category = categoryList.get(0);
        category.setCategoryName(categoryUpdateDTO.newName());
        categoryRepository.save(category);
    }
}
