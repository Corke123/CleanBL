package org.unibl.etf.ps.cleanbl.exception;

public class EmailTakenException extends IllegalArgumentException {
    public EmailTakenException() {
        super();
    }

    public EmailTakenException(String message) {
        super(message);
    }
}
