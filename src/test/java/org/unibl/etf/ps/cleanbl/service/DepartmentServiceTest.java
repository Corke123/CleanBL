package org.unibl.etf.ps.cleanbl.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unibl.etf.ps.cleanbl.model.Department;
import org.unibl.etf.ps.cleanbl.repository.DepartmentRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    private DepartmentService underTest;

    @BeforeEach
    public void init() {
        underTest = new DepartmentService(departmentRepository);
    }

    @Test
    public void itShouldReturnDepartments() {
        // Given
        List<Department> departmentList = Arrays.asList(
                new Department(1L, "Test1 Department", "051/111-111", "department1@mail.com", null),
                new Department(2L, "Test2 Department", "051/222-222", "department2@mail.com", null),
                new Department(3L, "Test3 Department", "051/333-333", "department3@mail.com", null)
        );

        given(departmentRepository.findAll()).willReturn(departmentList);

        // When
        List<Department> resultList = underTest.getDepartments();

        // Then
        assertThat(resultList.size() == 3);
    }

}