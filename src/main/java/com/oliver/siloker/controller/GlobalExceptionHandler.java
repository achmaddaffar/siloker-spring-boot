package com.oliver.siloker.controller;

import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleResourceNotFound(ResourceNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new BaseResponse<>(
                        HttpStatus.NOT_FOUND.value(),
                        e.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Object>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(new BaseResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<BaseResponse<Object>> handleIllegalState(IllegalStateException e) {
        return ResponseEntity
                .badRequest()
                .body(new BaseResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGenericException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse<>(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Unexpected error: " + e.getMessage(),
                        null
                ));
    }
}
