package com.petconomy.controller.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record MemberRegistrationDto(@JsonAlias("username") String name, String email, String password) {
}
