package org.unibl.etf.ps.cleanbl.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.Department;
import org.unibl.etf.ps.cleanbl.model.User;
import org.unibl.etf.ps.cleanbl.repository.DepartmentServiceRepository;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;
import org.unibl.etf.ps.cleanbl.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DepartmentServiceServiceImplTest {

    @InjectMocks
    DepartmentServiceServiceImpl departmentServiceService;

    @Mock
    DepartmentServiceRepository departmentServiceRepository;

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDepartmentServicesForUsersDepartment_should_return_list_of_department_services() {
        Department department = Department.builder().id(1L).name("Department").build();
        User user = User.builder().username("nnikolic").id(1L).department(department).build();

        org.springframework.security.core.userdetails.User userSpring =
                (org.springframework.security.core.userdetails.User)
                        org.springframework.security.core.userdetails.User.builder()
                                .username("nnikolic")
                                .accountLocked(false)
                                .accountExpired(false)
                                .credentialsExpired(false)
                                .authorities("ROLE_DepartmentOfficer")
                                .password("1234")
                                .build();

        List<DepartmentService> departmentServices = Arrays.asList(
                DepartmentService.builder().id(1L).name("Department 1").department(department).build(),
                DepartmentService.builder().id(2L).name("Department 2").department(department).build());

        when(userService.getCurrentlyLoggedInUser()).thenReturn(userSpring);
        when(userService.getUserByUsername(userSpring.getUsername())).thenReturn(user);
        when(departmentServiceRepository.findAllByDepartmentName(department.getName())).thenReturn(departmentServices);

        assertEquals(departmentServices, departmentServiceService.getDepartmentServicesForUsersDepartment());
    }

    @Test
    void getByName_should_return_department_service_with_given_name() {
        DepartmentService departmentService = DepartmentService.builder().id(1L).name("Department service").build();

        when(departmentServiceRepository.findByName(departmentService.getName())).thenReturn(Optional.of(departmentService));

        assertEquals(departmentService, departmentServiceService.getByName(departmentService.getName()));
    }

    @Test
    void getByName_should_throws_exception_when_department_service_not_exist() {
        String name = "Department";

        assertThrows(RecordNotFoundException.class, () -> departmentServiceService.getByName(name));
    }
}
