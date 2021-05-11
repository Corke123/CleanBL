package org.unibl.etf.ps.cleanbl.controller.error;

import lombok.Data;

@Data
public class FieldErrorDTO {
    private FieldErrorType type;
    private String object;
    private String field;
    private String message;
}
