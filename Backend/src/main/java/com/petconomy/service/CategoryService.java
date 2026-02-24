package com.petconomy.service;

import com.petconomy.controller.dto.CategoryDto;
import com.petconomy.model.transaction.Category;
import com.petconomy.model.user.Member;
import com.petconomy.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto>getAllVisibleCategories(Member member){
        List<Category> categories = categoryRepository.findByDefaultValueTrueOrMember(member);

        return categories.stream()
                .map(CategoryDto::new)
                .toList();
    }

    public Category createCustomCategory(String name, String color, BigDecimal targetAmount, Member member){
        if (categoryRepository.existsByNameAndMember(name, member)) {
            throw new RuntimeException("Category already exists!");
        }
        Category newCategory = new Category(name,color, member);

        newCategory.setColor((color == null || color.isEmpty()) ? "#696969" : color);
        newCategory.setTargetAmount((targetAmount != null) ? targetAmount : BigDecimal.ZERO);
        return categoryRepository.save(newCategory);
    }

    public CategoryDto updateCategoryBudget(Long categoryId, BigDecimal newAmount, Member member) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found for id: " + categoryId));
        if(category.isDefaultValue() || !Objects.equals(category.getMember(), member)){
            throw new RuntimeException("Unauthorized to update this category!");
        }
        category.setTargetAmount(newAmount);

        Category updated = categoryRepository.save(category);
        return new CategoryDto(updated);
    }

    public void deleteCategory(Long id, Member member){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found for id: " + id));

        if(category.isDefaultValue()){
            throw new RuntimeException("Cannot delete system category!");
        }

        if(!Objects.equals(category.getMember(), member)){
            throw new RuntimeException("Unauthorized to delete this category!");
        }


        categoryRepository.deleteCategoryById(id);
    }
}
