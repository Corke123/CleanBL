package org.unibl.etf.ps.cleanbl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {

    @NotBlank(message = "Department name is required")
    private String departmentName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "Part of the city is required")
    private String partOfTheCity;

    private String base64Image;
}
