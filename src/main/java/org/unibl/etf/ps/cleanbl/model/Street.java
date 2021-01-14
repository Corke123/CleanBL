package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Street {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Street name is required")
    private String name;

    @ManyToOne
    @JoinColumn(
            name = "partOfTheCityId"
    )
    private PartOfTheCity partOfTheCity;
}
