package com.petconomy.controller.dto;

import java.math.BigDecimal;

public record MemberProfileDto(Long id, String username, String email, BigDecimal targetAmount) {
}
