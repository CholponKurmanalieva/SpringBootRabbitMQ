package com.example.activator.handler;

import com.example.activator.dto.ResponseException;
import com.example.activator.exception.FileNotFoundException;
import com.example.activator.exception.FileSystemResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> fileNotFoundException(Exception e) {
        ResponseException response = ResponseException.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileSystemResourceException.class)
    public ResponseEntity<?> fileSystemResourceException(Exception e) {
        ResponseException response = ResponseException.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}