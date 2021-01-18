package org.unibl.etf.ps.cleanbl.exception;

import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidQueryExceptionHandler {
    @ExceptionHandler(value = {
            NonTransientDataAccessException.class
    })
    public ResponseEntity<Object> handleBadRequests(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
