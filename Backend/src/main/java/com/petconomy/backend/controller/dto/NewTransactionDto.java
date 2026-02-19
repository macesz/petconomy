package com.petconomy.backend.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public record NewTransactionDto(String name, List<Long> categoryIds, BigDecimal amount) {
}
