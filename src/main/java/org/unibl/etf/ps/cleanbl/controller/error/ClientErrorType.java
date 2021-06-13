package org.unibl.etf.ps.cleanbl.controller.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClientErrorType {
    VALIDATION_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "There are validation errors in the request"),
    RESOURCE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "Resource is not found"),
    EMAIL_OR_USERNAME_EXISTS(HttpStatus.CONFLICT, "Email or username is already taken"),
    DATABASE_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION(HttpStatus.CONFLICT, "Unable to delete record"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token provided"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Entity not found");

    private final HttpStatus httpStatus;

    private final String message;
}
