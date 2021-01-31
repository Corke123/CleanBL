package org.unibl.etf.ps.cleanbl.exception;

public class ReportNotFoundException extends IllegalArgumentException {
    public ReportNotFoundException() {
        super();
    }

    public ReportNotFoundException(String message) {
        super(message);
    }
}
