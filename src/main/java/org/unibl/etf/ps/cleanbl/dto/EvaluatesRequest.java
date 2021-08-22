package org.unibl.etf.ps.cleanbl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluatesRequest {

    @Min(value = 1, message = "The minimum grade is 1")
    @Max(value = 5, message = "Maximum grade is 5")
    private int grade;
}
