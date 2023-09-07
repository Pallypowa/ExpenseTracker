package com.pico.budgetapplication.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

    @NotBlank
    String categoryName;

    public CategoryDTO(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryDTO() {
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
