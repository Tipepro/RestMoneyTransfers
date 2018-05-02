package com.testTask.bank.exception;

public class AccountNotFoundException extends ClientAccountException {

    public AccountNotFoundException(String message) {
        super(message);
    }
}
