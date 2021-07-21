package org.unibl.etf.ps.cleanbl.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.unibl.etf.ps.cleanbl.dto.DepartmentOfficerDTO;
import org.unibl.etf.ps.cleanbl.mapper.UserMapper;
import org.unibl.etf.ps.cleanbl.model.Department;
import org.unibl.etf.ps.cleanbl.model.Role;
import org.unibl.etf.ps.cleanbl.model.User;
import org.unibl.etf.ps.cleanbl.model.UserStatus;
import org.unibl.etf.ps.cleanbl.service.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.unibl.etf.ps.cleanbl.fixtures.DepartmentFixture.createDepartment;
import static org.unibl.etf.ps.cleanbl.fixtures.RoleFixture.createRole;
import static org.unibl.etf.ps.cleanbl.fixtures.UserSpringFixture.*;
import static org.unibl.etf.ps.cleanbl.fixtures.UserStatusFixture.createUserStatus;

class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Mock
    UserMapper userMapper;

    @Mock
    UserStatusService userStatusService;

    @Mock
    DepartmentService departmentService;

    @Mock
    RoleService roleService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    PasswordGeneratorService passwordGeneratorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDepartmentOfficers_should_return_list_of_department_officers_dto() {
        List<DepartmentOfficerDTO> departmentOfficerDTOList = Collections.singletonList(
                createDepartmentOfficerDTO());
        Department department = createDepartment().build();
        List<User> users = Collections.singletonList(
                createUser().department(department).build());

        when(userService.getDepartmentOfficers()).thenReturn(users);
        when(userMapper.toDepartmentOfficer(users.get(0))).thenReturn(departmentOfficerDTOList.get(0));

        ResponseEntity<List<DepartmentOfficerDTO>> responseEntity = userController.getDepartmentOfficers();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(departmentOfficerDTOList, responseEntity.getBody());
    }

    @Test
    void addDepartmentOfficer_should_return_department_officers_dto() {
        DepartmentOfficerDTO departmentOfficerDTO = createDepartmentOfficerDTO();
        String password = "password123";
        UserStatus userStatus = createUserStatus().build();
        Department department = createDepartment().build();
        List<Role> rolesList = Collections.singletonList(
                createRole().build()
        );
        Role role = createRole().build();
        User toCreate = createUser().roles(rolesList).userStatus(userStatus).department(department).build();

        when(passwordGeneratorService.generateRandomPassword()).thenReturn(password);
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(userMapper.toUserFromDepartmentOfficerDTO(departmentOfficerDTO)).thenReturn(toCreate);
        when(roleService.getDepartmentOfficerRole()).thenReturn(role);
        when(userStatusService.getActiveStatus()).thenReturn(userStatus);
        when(departmentService.getByName(departmentOfficerDTO.getDepartment())).thenReturn(department);
        when(userService.addDepartmentOfficer(toCreate, password)).thenReturn(toCreate);
        when(userMapper.toDepartmentOfficer(toCreate)).thenReturn(departmentOfficerDTO);

        ResponseEntity<DepartmentOfficerDTO> responseEntity = userController.addDepartmentOfficer(departmentOfficerDTO);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(departmentOfficerDTO, responseEntity.getBody());
    }

    @Test
    void updateDepartmentOfficer_should_return_department_officers_dto() {
        Long id = 1L;
        DepartmentOfficerDTO departmentOfficerDTO = createDepartmentOfficerDTO();
        Department department = createDepartment().build();
        User user = createUser().department(department).build();

        when(userService.getById(id)).thenReturn(Optional.of(user));
        when(userMapper.updateUserFromDepartmentOfficerDTO(departmentOfficerDTO, user)).thenReturn(user);
        when(departmentService.getByName(departmentOfficerDTO.getDepartment())).thenReturn(department);
        when(userService.save(user)).thenReturn(user);
        when(userMapper.toDepartmentOfficer(user)).thenReturn(departmentOfficerDTO);

        ResponseEntity<DepartmentOfficerDTO> responseEntity = userController.updateDepartmentOfficer(id, departmentOfficerDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(departmentOfficerDTO, responseEntity.getBody());
    }

    @Test
    void updateDepartmentOfficer_should_return_response_entity_status_not_found() {
        Long id = 1L;
        DepartmentOfficerDTO departmentOfficerDTO = createDepartmentOfficerDTO();

        when(userService.getById(id)).thenReturn(Optional.empty());

        ResponseEntity<DepartmentOfficerDTO> responseEntity = userController.updateDepartmentOfficer(id, departmentOfficerDTO);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void deleteDepartmentOfficer_should_return_response_entity_status_ok() {
        Department department = createDepartment().build();
        UserStatus userStatus = createUserStatus().build();
        List<Role> rolesList = Collections.singletonList(
                       createRole().build()
                 );
        User user = createUser().roles(rolesList).userStatus(userStatus).department(department).build();

        ResponseEntity<Void> responseEntity = userController.deleteDepartmentOfficer(user.getId());
        verify(userService, times(1)).deleteDepartmentOfficer(user.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
