package com.ml.simios.handler;

import com.ml.simios.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> badRequestExceptionHandle(BadRequestException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
