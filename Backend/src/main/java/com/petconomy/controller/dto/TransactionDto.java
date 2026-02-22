package com.petconomy.controller.dto;

import com.petconomy.model.transaction.Category;
import com.petconomy.model.transaction.Transaction;

import java.time.LocalDate;

public record TransactionDto(Long id, String name, Category category, int amount, Long memberId, LocalDate date) {
    public TransactionDto(Transaction transaction){
        this(transaction.getId(), transaction.getName(), transaction.getCategory(), transaction.getAmount(), transaction.getMember().getId(), transaction.getDate());
    }
}
