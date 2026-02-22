package com.petconomy.controller.exception;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException() {
        super("Pet not found for the specified owner.");
    }
}
