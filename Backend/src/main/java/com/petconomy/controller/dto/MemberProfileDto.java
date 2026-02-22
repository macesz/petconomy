package com.petconomy.controller.dto;

import java.math.BigDecimal;

public record MemberProfileDto(int id, String username, String email, BigDecimal targetAmount) {
}
