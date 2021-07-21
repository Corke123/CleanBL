package org.unibl.etf.ps.cleanbl.fixtures;

import org.springframework.security.core.userdetails.User;
import org.unibl.etf.ps.cleanbl.dto.DepartmentOfficerDTO;

public class UserSpringFixture {

    public static User.UserBuilder crateUserSpring() {
        return org.springframework.security.core.userdetails.User.builder()
                .username("marko123")
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .authorities("ROLE_DepartmentOfficer")
                .password("marko123");
    }

    public static org.unibl.etf.ps.cleanbl.model.User.UserBuilder createUser() {
        return org.unibl.etf.ps.cleanbl.model.User.builder()
                .id(1L)
                .firstName("Marko")
                .lastName("Markovic")
                .username("marko123")
                .email("marko123@gmail.com");
    }

    public static DepartmentOfficerDTO createDepartmentOfficerDTO() {
        return new DepartmentOfficerDTO(1L,
                "Marko",
                "Markovic",
                "marko123",
                "marko123@gmail.com",
                "Department 1");
    }
}

