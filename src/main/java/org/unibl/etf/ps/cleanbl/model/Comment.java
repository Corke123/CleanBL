package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private Timestamp dateOfPublication;

    @NotBlank(message = "Content is required")
    private String content;

    @ManyToOne
    @JoinColumn(
            name = "userId"
    )
    private EndUser endUser;
}
