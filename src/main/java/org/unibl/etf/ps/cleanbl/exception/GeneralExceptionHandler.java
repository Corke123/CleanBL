package org.unibl.etf.ps.cleanbl.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(value = {
            ReportNotFoundException.class
    })
    public ResponseEntity<Object> handleNotFoundExceptions(ReportNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            EmailTakenException.class,
            UsernameTakenException.class
    })
    public ResponseEntity<Object> handleUsernameAndEmailConflicts(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            JWTVerificationException.class,
            VerificationTokenException.class
    })
    public ResponseEntity<Object> invalidLogin(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {
            RecordNotFoundException.class
    })
    public ResponseEntity<Object> handleInternalExceptions(RecordNotFoundException e) {
        return new ResponseEntity<>("A problem occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
