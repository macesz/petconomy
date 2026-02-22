package com.petconomy.controller.dto;

public record NewTransactionDto(String name, Long categoryId, int amount) {
}
