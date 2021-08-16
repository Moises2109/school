package com.example.school.exception;

import com.example.school.util.ErrorMessageUtils;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ENTITY_FOUND(HttpStatus.BAD_REQUEST, ErrorMessageUtils.MSG_ENTITY_FOUND),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorMessageUtils.MSG_ENTITY_NOT_FOUND);

    private HttpStatus httpStatus;

    private String code;

    private String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

     public String getMessage() {
        return this.errorMessage;
    }
}
