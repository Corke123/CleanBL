package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "endUserId"
    )
    private EndUser endUser;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Image path is required")
    private String imagePath;

    private Date createdAt;

    @ManyToOne
    @JoinColumn(
            name = "reportStatusId"
    )
    private ReportStatus reportStatus;

    @ManyToOne
    @JoinColumn(
            name = "streetId"
    )
    private Street street;

    @ManyToOne
    @JoinColumn(
            name = "departmentId"
    )
    private Department department;

    @PrePersist
    public void placedAt() {
        createdAt = new Date();
    }

    public Report(EndUser endUser, String description, String imagePath, ReportStatus reportStatus, Street street, Department department) {
        this.endUser = endUser;
        this.description = description;
        this.imagePath = imagePath;
        this.reportStatus = reportStatus;
        this.street = street;
        this.department = department;
    }
}
