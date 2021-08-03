package org.unibl.etf.ps.cleanbl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
    @NotBlank(message = "Department name is required.")
    private String name;
    private int id;
    private String phone;
    private String email;
}
