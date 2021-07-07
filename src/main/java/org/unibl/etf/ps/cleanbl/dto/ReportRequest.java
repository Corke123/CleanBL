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

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Latitude is required")
    private Double latitude;

    @NotBlank(message = "Longitude is required")
    private Double longitude;

    private String base64Image;
}
