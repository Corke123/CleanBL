package org.unibl.etf.ps.cleanbl.controller.error;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationClientErrorDTO extends ClientErrorDTO {
    private final List<FieldErrorDTO> fieldErrors;

    public ValidationClientErrorDTO(ClientErrorType type, String message, List<FieldErrorDTO> fieldErrors) {
        super(type, message);
        this.fieldErrors = fieldErrors;
    }
}
