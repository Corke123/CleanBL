package org.unibl.etf.ps.cleanbl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.unibl.etf.ps.cleanbl.model.EndUser;
import org.unibl.etf.ps.cleanbl.model.UserStatus;
import org.unibl.etf.ps.cleanbl.repository.EndUserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class EndUserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    EndUserRepository endUserRepository;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        // given
        EndUser john = EndUser.builder()
                .firstName("John")
                .lastName("Smith")
                .username("js1234")
                .email("john.smith@mail.com")
                .password("password")
                .numberOfNegativePoints(0)
                .numberOfPositivePoints(0)
                .userStatus(new UserStatus(1L, "inactive"))
                .build();

        entityManager.persist(john);
        entityManager.flush();

        // when
        Optional<EndUser> found = endUserRepository.findByUsername(john.getUsername());

        // then
        assertThat(found.get().getUsername())
                .isEqualTo(john.getUsername());
    }
}
