package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Department name is required")
    private String name;

    @Pattern(
            regexp = "(051|\\+387\\s*51)/[0-9]{3}-[0-9]{3}",
            message = "Phone must be formatted like 051/xxx-yyy or +387 51/xxx-yyy"
    )
    private String phone;

    @Email
    @NotBlank(message = "E-mail is required")
    private String email;
}