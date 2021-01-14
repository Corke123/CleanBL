package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EvaluatesId implements Serializable {

    @ManyToOne
    @JoinColumn(
            name = "reportId"
    )
    private Report report;

    @ManyToOne
    @JoinColumn(
            name = "endUserId"
    )
    private EndUser endUser;

    @ManyToOne
    @JoinColumn(
            name = "departmentServiceId"
    )
    private DepartmentService departmentService;
}
