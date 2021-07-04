package org.unibl.etf.ps.cleanbl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private Long id;
    private String userReported;
    private String description;
    private LocalDate createdAt;
    private String status;
    private String street;
    private String partOfTheCity;
    private String department;
    private String departmentService;
    private Boolean valid;
    private LocalDate processed;
    private String base64Image;
}
