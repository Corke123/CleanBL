package org.unibl.etf.ps.cleanbl.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RegistrationExceptionHandler {

    @ExceptionHandler(value = {
            EmailTakenException.class,
            UsernameTakenException.class,
            VerificationTokenException.class
    })
    public ResponseEntity<Object> handleBadRequests(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            JWTVerificationException.class
    })
    public ResponseEntity<Object> invalidLogin(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
