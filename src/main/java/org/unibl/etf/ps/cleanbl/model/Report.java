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

    @Column(
            name = "image",
            length = 1024
    )
    private byte[] imageBytes;

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
            name = "departmentOfficerId"
    )
    private DepartmentOfficer departmentOfficer;

    @PrePersist
    public void placedAt() {
        createdAt = new Date();
    }
}
