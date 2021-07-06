package org.unibl.etf.ps.cleanbl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.Role;
import org.unibl.etf.ps.cleanbl.repository.RoleRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RoleServiceTest {
    @InjectMocks
    RoleService roleService;

    @Mock
    RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserRole_should_return_role_user() {
        Role role = Role.builder().name("User").id(1L).build();
        when(roleRepository.findByName("User")).thenReturn(Optional.of(role));
        assertEquals(role,roleService.getUserRole());
    }

    @Test
    void getDepartmentOfficerRole_should_return_role_department_offices(){
        Role role = Role.builder().name("DepartmentOfficer").id(1L).build();
        when(roleRepository.findByName("DepartmentOfficer")).thenReturn(Optional.of(role));
        assertEquals(role,roleService.getDepartmentOfficerRole());
    }

    @Test
    void getUserRole_should_throws_exception_when_role_not_exist() {
        when(roleRepository.findByName("User")).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> roleService.getUserRole());
    }

    @Test
    void getDepartmentOfficerRole_should_throws_exception_when_role_not_exist() {
        when(roleRepository.findByName("DepartmentOfficer")).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> roleService.getDepartmentOfficerRole());
    }

}
