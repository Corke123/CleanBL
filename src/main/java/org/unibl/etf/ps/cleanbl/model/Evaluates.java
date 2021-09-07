package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Evaluates {

    @EmbeddedId
    private EvaluatesId id;

    private LocalDateTime gradedAt;

    private int grade;

    @PrePersist
    public void placedAt() {
        gradedAt = LocalDateTime.now();
    }
}
