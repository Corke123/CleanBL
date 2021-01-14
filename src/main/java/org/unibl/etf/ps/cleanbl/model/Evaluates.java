package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

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
            name = "endUserId"
    )
    private EndUser endUser;

    @Id
    @ManyToOne
    @JoinColumn(
            name = "departmentServiceId"
    )
    private DepartmentService departmentService;

    @DecimalMin(value = "0.0", message = "Grade can't be less than 0")
    @DecimalMax(value = "5.0", message = "Grade can't be greater than 5")
    @Column(columnDefinition = "DECIMAL(2)")
    private Double grade;
}
