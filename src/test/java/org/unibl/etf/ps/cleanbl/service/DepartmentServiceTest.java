package org.unibl.etf.ps.cleanbl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unibl.etf.ps.cleanbl.model.Department;
import org.unibl.etf.ps.cleanbl.model.Role;
import org.unibl.etf.ps.cleanbl.model.User;
import org.unibl.etf.ps.cleanbl.repository.DepartmentRepository;
import org.unibl.etf.ps.cleanbl.repository.RoleRepository;
import org.unibl.etf.ps.cleanbl.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DepartmentServiceTest {
    @InjectMocks
    DepartmentService departmentService;

    @Mock
    DepartmentRepository departmentRepository;

    @Mock
    RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
        @Test
    void getByDepartmentName_should_return_department_name() {
            Department department = new Department();
            department.setName("Komunalni poslovi");
            department.setEmail("komunalno@gmail.com");
            department.setPhone("051/222-222");
            department.setId(1L);
        when(departmentRepository.findByName("Komunalni poslovi")).thenReturn(Optional.of(department));
        assertEquals(department,departmentService.getByName("Komunalni poslovi"));
    }

    @Test
    void getDepartments_should_return_all_departments () {
        List<Department> departments = departmentRepository.findAll();
        assertEquals(departments,departmentService.getDepartments());
    }

}