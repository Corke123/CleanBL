package org.unibl.etf.ps.cleanbl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.unibl.etf.ps.cleanbl.model.Department;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository underTest;

    @Test
    public void itShouldFindDepositoryByName() {
        // Given
        Department department = new Department(
                null, "Test Department", "051/111-222", "department@mail.com", null);

        // When
        underTest.save(department);

        // Then
        Optional<Department> optionalDepartment = underTest.findByName("Test Department");
        assertThat(optionalDepartment)
                .isPresent()
                .hasValueSatisfying(d -> assertThat(d).isEqualTo(department));
    }

    @Test
    public void itNotShouldSelectDepartmentByNameWhenNameDoesNotExists() {
        // Given
        String name = "Test Department";

        // When
        Optional<Department> optionalDepartment = underTest.findByName(name);

        // Then
        assertThat(optionalDepartment).isNotPresent();
    }
}