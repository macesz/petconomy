package com.petconomy.backend.controller.dto;

import java.math.BigDecimal;

public record UpdateProfileDto(String currentPassword,
                               String email,
                               String newPassword,
                               String username,
                               String firstName,
                               String lastName,
                               String houseName,
                               BigDecimal newTargetAmount,
                               String connectToUserEmail ) {
}
