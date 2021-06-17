package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "reportId"
    )
    private Report report;

    private LocalDate createdAt;

    private String content;

    @ManyToOne
    @JoinColumn(
            name = "userId"
    )
    private User user;

    @PrePersist
    private void setDateOfPublication() {
        this.createdAt = LocalDate.now();
    }

}
