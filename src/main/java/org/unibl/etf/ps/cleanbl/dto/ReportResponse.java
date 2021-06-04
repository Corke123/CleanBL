package org.unibl.etf.ps.cleanbl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private Long id;
    private String userReported;
    private String description;
    private Date createdAt;
    private String status;
    private String street;
    private String partOfTheCity;
    private String department;
    private String base64Image;
}
