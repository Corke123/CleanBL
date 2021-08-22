package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class EvaluatesId implements Serializable {

    private static final long serialVersionUID = -5340494269474535260L;

    @ManyToOne
    @JoinColumn (
            name = "reportId"
    )
    private Report report;

    @ManyToOne
    @JoinColumn(
            name = "userId"
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "departmentServiceId"
    )
    private DepartmentService departmentService;
}
