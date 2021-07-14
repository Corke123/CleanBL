package org.unibl.etf.ps.cleanbl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

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

    @DecimalMin(value = "-90", message = "Minimum valid latitude is -90")
    @DecimalMax(value = "90", message = "Maximum valid latitude is 90")
    private BigDecimal latitude;

    @DecimalMin(value = "-180", message = "Minimum valid latitude is -180")
    @DecimalMax(value = "180", message = "Maximum valid latitude is 180")
    private BigDecimal longitude;

    @NotBlank(message = "Image is required")
    private String base64Image;
}
