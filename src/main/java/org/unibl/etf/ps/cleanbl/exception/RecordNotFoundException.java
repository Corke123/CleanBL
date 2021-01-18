package org.unibl.etf.ps.cleanbl.exception;

import org.springframework.dao.NonTransientDataAccessException;

public class RecordNotFoundException extends NonTransientDataAccessException {
    public RecordNotFoundException(String msg) {
        super(msg);
    }
}
