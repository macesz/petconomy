package com.petconomy.controller.dto;

import com.petconomy.model.transaction.Category;

public record CategoryDto(Long id, Category.CategoryType categoryType, String description, String color) {
    public CategoryDto(Category category) {
        this(category.getId(), category.getType(), category.getDescription(), category.getColor());
    }
}
