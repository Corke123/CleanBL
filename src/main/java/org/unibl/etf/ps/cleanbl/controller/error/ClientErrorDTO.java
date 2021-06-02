package org.unibl.etf.ps.cleanbl.controller.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ClientErrorDTO {
    private ClientErrorType type;
    private String message;
}
