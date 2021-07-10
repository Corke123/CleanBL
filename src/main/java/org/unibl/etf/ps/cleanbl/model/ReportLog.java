package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Slf4j
@Entity
public class ReportLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "userId"
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "reportId"
    )
    private Report report;

    private String description;

    private LocalDateTime changedAt;

    @PrePersist
    public void placedAt(){ changedAt = LocalDateTime.now();}

}
