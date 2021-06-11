package org.unibl.etf.ps.cleanbl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreetRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Part of the city is required")
    private String partOfTheCity;
}