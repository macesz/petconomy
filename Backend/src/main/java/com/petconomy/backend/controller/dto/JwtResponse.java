package com.petconomy.backend.controller.dto;

import java.util.List;

public record JwtResponse(String jwt, String userName, List<String> roles) {
}