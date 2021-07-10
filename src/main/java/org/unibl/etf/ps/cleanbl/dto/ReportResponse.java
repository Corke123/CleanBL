package org.unibl.etf.ps.cleanbl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private Long id;
    private String userReported;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private String status;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String department;
    private String departmentService;
    private Boolean valid;
    private LocalDate processed;
    private String base64Image;
}
