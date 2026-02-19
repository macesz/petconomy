package com.petconomy.backend.controller.exception;

public class UserEntityNotFoundException extends RuntimeException {
    public UserEntityNotFoundException() {
        super("User not Found");
    }
}
