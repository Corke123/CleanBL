package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(EvaluatesId.class)
public class Evaluates {

    @Id
    @ManyToOne
    @JoinColumn(
            name = "reportId"
    )
    private Report report;

    @Id
    @ManyToOne
    @JoinColumn(
            name = "userId"
    )
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(
            name = "departmentServiceId"
    )
    private DepartmentService departmentService;

    private int grade;
}
