package org.unibl.etf.ps.cleanbl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.unibl.etf.ps.cleanbl.model.UserStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none",
        }
)
public class UserStatusRepositoryTest {

    @Autowired
    private UserStatusRepository underTest;

    @Test
    public void itShouldFindUserStatusByName() {
        // Given
        UserStatus userStatus = new UserStatus(null, "active");

        // When

        underTest.save(userStatus);

        // Then
        Optional<UserStatus> optionalUserStatus = underTest.findByName("active");
        assertThat(optionalUserStatus)
                .isPresent()
                .hasValueSatisfying(d -> assertThat(d).isEqualTo(userStatus));
    }

    @Test
    public void itNotShouldSelectUserStatusByNameWhenNameDoesNotExists() {
        // Given
        String name = "active";

        // When
        Optional<UserStatus> optionalUserStatus = underTest.findByName(name);

        // Then
        assertThat(optionalUserStatus).isNotPresent();
    }
}