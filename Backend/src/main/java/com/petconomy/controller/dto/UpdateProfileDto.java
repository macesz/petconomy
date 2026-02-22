package com.petconomy.controller.dto;

import java.math.BigDecimal;

public record UpdateProfileDto(String currentPassword, String email, String newPassword, String username, BigDecimal newTargetAmount) {
}
