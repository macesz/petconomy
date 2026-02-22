package com.petconomy.controller.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("User not Found");
    }
}
