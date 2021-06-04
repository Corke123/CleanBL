package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
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

    public String encodeImage(String uploadDir) {
        Path path = Paths.get(uploadDir + imagePath);
        String encodedString = "";
        try {
            encodedString = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
        } catch (IOException e) {
            log.info("Unable to read image from: " + path);
        }
        return encodedString;
    }

    public boolean canUserEditReport(org.springframework.security.core.userdetails.User user) {
        return endUser.getUsername().equals(user.getUsername());
    }
}
