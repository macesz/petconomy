package com.petconomy.controller.dto;

import com.petconomy.model.transaction.Category;

import java.math.BigDecimal;

public record CategoryDto(
        Long id,
        String categoryType,
        String name,
        String color,
        BigDecimal targetAmount,
        boolean isSystem
) {
    public CategoryDto(Category category) {
        this(
                category.getId(),
                category.getType() != null ? category.getType().name() : "CUSTOM",
                category.getName(),
                category.getColor(),
                category.getTargetAmount(),
                category.isDefaultValue()
        );
    }
}
