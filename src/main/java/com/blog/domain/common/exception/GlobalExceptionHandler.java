package com.blog.domain.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResult> GlobalHandler(GlobalException e) {
        return new ResponseEntity<>(new ErrorResult(e.getMessage()), e.getHttpStatus());
    }
}
