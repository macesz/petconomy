package com.petconomy.controller;

import com.petconomy.controller.dto.CategoryDto;
import com.petconomy.model.transaction.Category;
import com.petconomy.model.user.Member;
import com.petconomy.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public List<CategoryDto> getAllCategories(@AuthenticationPrincipal Member member){
        return categoryService.getAllVisibleCategories(member);
    }

    @PostMapping("/create")
    public ResponseEntity <CategoryDto> createCustomCategory(String name, String color, BigDecimal targetAmount, @AuthenticationPrincipal Member member){
        Category newCategory = categoryService.createCustomCategory(name, color, targetAmount, member);
        return ResponseEntity.ok(new CategoryDto(newCategory));
    }

    @PutMapping("/{id}/budget")
    public ResponseEntity<?> updateCategoryBudget(@PathVariable Long id,
                                                  @RequestParam BigDecimal newAmount,
                                                  @AuthenticationPrincipal Member member) {
        CategoryDto updated = categoryService.updateCategoryBudget(id, newAmount, member);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable Long id, @AuthenticationPrincipal Member member){
        categoryService.deleteCategory(id, member);
    }

}
