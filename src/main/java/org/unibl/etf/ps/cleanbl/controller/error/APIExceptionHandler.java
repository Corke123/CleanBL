package org.unibl.etf.ps.cleanbl.controller.error;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.unibl.etf.ps.cleanbl.exception.*;

import java.util.ArrayList;

@ControllerAdvice
@Slf4j
public class APIExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ValidationClientErrorDTO> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var fieldErrors = new ArrayList<FieldErrorDTO>();

        for (var bindingFieldError : exception.getBindingResult().getFieldErrors()) {
            var fieldError = new FieldErrorDTO();
            fieldError.setType("NotNull".equals(bindingFieldError.getCode()) || "NotBlank".equals(bindingFieldError.getCode()) || "NotEmpty".equals(bindingFieldError.getCode())
                    ? FieldErrorType.REQUIRED
                    : FieldErrorType.INVALID);
            fieldError.setObject(bindingFieldError.getObjectName());
            fieldError.setField(bindingFieldError.getField());
            fieldError.setMessage(bindingFieldError.getDefaultMessage());

            fieldErrors.add(fieldError);
        }

        var errorType = ClientErrorType.VALIDATION_ERROR;
        var validationClientError = new ValidationClientErrorDTO(errorType, errorType.getMessage(), fieldErrors);
        log.warn("Exception during api call: " + exception);
        return new ResponseEntity<>(validationClientError, errorType.getHttpStatus());
    }

    @ExceptionHandler(value = {
            EmailTakenException.class,
            UsernameTakenException.class
    })
    public ResponseEntity<Object> handleUsernameAndEmailConflicts(IllegalArgumentException exception) {
        ClientErrorType errorType = ClientErrorType.EMAIL_OR_USERNAME_EXISTS;
        ClientErrorDTO clientErrorDTO = new ClientErrorDTO(errorType, exception.getMessage());
        log.warn("Exception during api call: " + exception);
        return new ResponseEntity<>(clientErrorDTO, clientErrorDTO.getType().getHttpStatus());
    }

    @ExceptionHandler(value = {RecordNotFoundException.class})
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException exception) {
        ClientErrorType errorType = ClientErrorType.RESOURCE_NOT_FOUND;
        ClientErrorDTO clientErrorDTO = new ClientErrorDTO(errorType, exception.getMessage());
        log.error("Exception during api call: " + exception);
        return new ResponseEntity<>(clientErrorDTO, clientErrorDTO.getType().getHttpStatus());
    }

    @ExceptionHandler(value = {
            JWTVerificationException.class,
            VerificationTokenException.class
    })
    public ResponseEntity<Object> invalidLogin(VerificationTokenException exception) {
        ClientErrorType errorType = ClientErrorType.INVALID_TOKEN;
        ClientErrorDTO clientErrorDTO = new ClientErrorDTO(errorType, exception.getMessage());
        log.warn(exception.getMessage());
        return new ResponseEntity<>(clientErrorDTO, clientErrorDTO.getType().getHttpStatus());
    }

    @ExceptionHandler(value = ReportNotFoundException.class)
    public ResponseEntity<Object> reportNotFoundException(ReportNotFoundException exception) {
        ClientErrorType errorType = ClientErrorType.NOT_FOUND;
        ClientErrorDTO clientErrorDTO = new ClientErrorDTO(errorType, exception.getMessage());
        log.warn(exception.getMessage());
        return new ResponseEntity<>(clientErrorDTO, clientErrorDTO.getType().getHttpStatus());
    }
}
