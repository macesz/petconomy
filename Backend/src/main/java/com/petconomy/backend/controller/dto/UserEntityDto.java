package com.petconomy.backend.controller.dto;

import com.petconomy.backend.model.entity.UserEntity;
import com.petconomy.backend.model.entity.Transaction;

import java.math.BigDecimal;
import java.util.Set;

public record UserEntityDto(Long id, String name, String email, BigDecimal target, Set<Transaction> transactions) {
    public UserEntityDto(UserEntity userEntity) {
        this(userEntity.getId(), userEntity.getUserName(), userEntity.getEmail(), userEntity.getTargetAmount(), userEntity.getTransactions());
    }
}
