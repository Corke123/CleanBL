package org.unibl.etf.ps.cleanbl.exception;

public class UsernameTakenException extends IllegalArgumentException {
    public UsernameTakenException() {
        super();
    }

    public UsernameTakenException(String message) {
        super(message);
    }
}
