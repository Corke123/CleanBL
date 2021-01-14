package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DepartmentService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @Pattern(
            regexp = "(051|\\+387\\s*51)/[0-9]{3}-[0-9]{3}",
            message = "Phone must be formatted like 051/xxx-yyy or +387 51/xxx-yyy"
    )
    private String phone;

    @ManyToOne
    @JoinColumn(
            name = "departmentId"
    )
    private Department department;
}
