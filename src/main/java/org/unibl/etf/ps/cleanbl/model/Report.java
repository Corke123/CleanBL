package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;

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
            name = "userId"
    )
    private User user;

    private String description;

    private String imagePath;

    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(
            name = "statusId"
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

    @ManyToOne
    @JoinColumn(
            name = "departmentServiceId"
    )
    private DepartmentService departmentService;

    @PrePersist
    public void placedAt() {
        createdAt = LocalDate.now();
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
        return user.getUsername().equals(user.getUsername());
    }
}
