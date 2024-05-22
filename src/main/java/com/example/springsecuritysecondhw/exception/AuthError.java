package com.example.springsecuritysecondhw.exception;

import lombok.Data;

import java.util.Date;

@Data
public class AuthError {
    private String message;
    private int status;
    private Date timestamp;

    public AuthError(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = new Date();
    }
}
