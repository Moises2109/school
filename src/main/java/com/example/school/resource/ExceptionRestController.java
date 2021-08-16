package com.example.school.resource;

import com.example.school.exception.BadRequestException;
import com.example.school.exception.ErrorCode;
import com.example.school.util.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionRestController {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<DataResponse> handleEntityNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DataResponse.error(ErrorCode.ENTITY_NOT_FOUND, ErrorCode.ENTITY_NOT_FOUND.getMessage() ));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<DataResponse> handleBadRequestException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DataResponse.error(ErrorCode.ENTITY_FOUND, ErrorCode.ENTITY_FOUND.getMessage()));
    }

}
