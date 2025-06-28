package com.debtapp.debt.controller.exceptions;

public class DebtNotFoundException extends RuntimeException {
    public DebtNotFoundException(String message) {
        super(message);
    }
}
