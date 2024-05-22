package com.example.springsecuritysecondhw.exception;

import lombok.Data;

@Data
public class RegisterError {
    private String message;
    private int status;

    public RegisterError(String message, int status) {
        this.message = message;
        this.status = status;
    }

}
