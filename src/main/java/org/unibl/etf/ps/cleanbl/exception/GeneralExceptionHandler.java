package org.unibl.etf.ps.cleanbl.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler {

    @ExceptionHandler(value = {
            ReportNotFoundException.class
    })
    public ResponseEntity<Object> handleNotFoundExceptions(ReportNotFoundException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            EmailTakenException.class,
            UsernameTakenException.class
    })
    public ResponseEntity<Object> handleUsernameAndEmailConflicts(IllegalArgumentException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            JWTVerificationException.class,
            VerificationTokenException.class
    })
    public ResponseEntity<Object> invalidLogin(RuntimeException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {
            RecordNotFoundException.class
    })
    public ResponseEntity<Object> handleInternalExceptions(RecordNotFoundException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>("A problem occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
