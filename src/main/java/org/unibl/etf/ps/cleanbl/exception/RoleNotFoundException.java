package org.unibl.etf.ps.cleanbl.exception;

import org.springframework.dao.NonTransientDataAccessException;

public class RoleNotFoundException extends NonTransientDataAccessException {
    public RoleNotFoundException(String msg) {
        super(msg);
    }
}
