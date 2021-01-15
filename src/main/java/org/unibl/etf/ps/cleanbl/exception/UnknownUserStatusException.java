package org.unibl.etf.ps.cleanbl.exception;

import org.springframework.dao.NonTransientDataAccessException;

public class UnknownUserStatusException extends NonTransientDataAccessException {
    public UnknownUserStatusException(String msg) {
        super(msg);
    }
}
