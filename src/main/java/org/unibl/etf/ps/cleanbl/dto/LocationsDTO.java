package org.unibl.etf.ps.cleanbl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationsDTO {
    private Long id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status;
}
