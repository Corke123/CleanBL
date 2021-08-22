package org.unibl.etf.ps.cleanbl.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.unibl.etf.ps.cleanbl.model.Department;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;
import org.unibl.etf.ps.cleanbl.service.DepartmentServiceService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DepartmentServiceControllerTest {

    @InjectMocks
    DepartmentServiceController departmentServiceController;

    @Mock
    DepartmentServiceService departmentServiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDepartmentServices_should_return_list_of_department_services() {
        Department department = Department.builder().id(1L).name("Department").build();

        List<DepartmentService> departmentServices = Arrays.asList(
                DepartmentService.builder().id(1L).name("Department 1").department(department).build(),
                DepartmentService.builder().id(2L).name("Department 2").department(department).build());

        when(departmentServiceService.getDepartmentServicesForUsersDepartment()).thenReturn(departmentServices);

        //ResponseEntity<List<DepartmentService>> responseEntity = departmentServiceController.getDepartmentsDTO();

        //assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        //assertEquals(departmentServices, responseEntity.getBody());
    }
}
