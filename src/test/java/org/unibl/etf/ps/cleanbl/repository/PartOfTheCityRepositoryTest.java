package org.unibl.etf.ps.cleanbl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
@AutoConfigureTestDatabase(replace = NONE)
public class PartOfTheCityRepositoryTest {
    @Autowired
    private PartOfTheCityRepository underTest;

    @Test
    public void itShouldFindPartOfTheCityByName() {
        // Given
        PartOfTheCity partOfTheCity = new PartOfTheCity(null, "City Block", null);

        // When
        underTest.save(partOfTheCity);

        // Then
        Optional<PartOfTheCity> partOfTheCityOptional = underTest.findByName("City Block");
        assertThat(partOfTheCityOptional)
        .isPresent()
        .hasValueSatisfying(p -> assertThat(p).isEqualTo(partOfTheCity));
    }

    @Test
    public void itNotShouldFindDepartmentByNameWhenNameDoesNotExists() {
        // Given
        String name = "City Block";

        // When
        Optional<PartOfTheCity> optionalPartOfTheCity = underTest.findByName("City Block");

        // Then
        assertThat(optionalPartOfTheCity).isNotPresent();
    }

}