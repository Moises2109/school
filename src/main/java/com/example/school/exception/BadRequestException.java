package com.example.school.exception;

public class BadRequestException extends Exception {

    private String message;

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}