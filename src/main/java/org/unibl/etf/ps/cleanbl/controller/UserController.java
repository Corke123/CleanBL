package org.unibl.etf.ps.cleanbl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.DepartmentOfficerDTO;
import org.unibl.etf.ps.cleanbl.mapper.UserMapper;
import org.unibl.etf.ps.cleanbl.model.User;
import org.unibl.etf.ps.cleanbl.service.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserStatusService userStatusService;
    private final DepartmentService departmentService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGeneratorService passwordGeneratorService;

    @GetMapping("/department-officers")
    @PreAuthorize("hasAnyAuthority('DepartmentOfficer_Read', 'ROLE_Admin')")
    ResponseEntity<List<DepartmentOfficerDTO>> getDepartmentOfficers() {
        return ResponseEntity.ok(userService.getDepartmentOfficers().stream()
                .map(userMapper::toDepartmentOfficer)
                .collect(Collectors.toList()));
    }

    @PostMapping("/department-officers")
    @PreAuthorize("hasAnyAuthority('DepartmentOfficer_Create', 'ROLE_Admin')")
    public ResponseEntity<DepartmentOfficerDTO> addDepartmentOfficer(@Valid @RequestBody DepartmentOfficerDTO departmentOfficerDTO) {
        String password = passwordGeneratorService.generateRandomPassword();
        User toCreate = userMapper.toUserFromDepartmentOfficerDTO(departmentOfficerDTO);
        toCreate.setPassword(passwordEncoder.encode(password));
        toCreate.setDepartment(departmentService.getByName(departmentOfficerDTO.getDepartment()));
        toCreate.setUserStatus(userStatusService.getActiveStatus());
        toCreate.setRoles(Collections.singletonList(roleService.getDepartmentOfficerRole()));

        DepartmentOfficerDTO created = userMapper.toDepartmentOfficer(userService.addDepartmentOfficer(toCreate, password));
        return ResponseEntity.created(URI.create("/api/v1/users/department-officers/" + created.getId())).body(created);
    }

    @PutMapping("/department-officers/{id}")
    @PreAuthorize("hasAnyAuthority('DepartmentOfficer_Update', 'ROLE_Admin')")
    public ResponseEntity<DepartmentOfficerDTO> updateDepartmentOfficer(
            @PathVariable("id") Long id,
            @RequestBody DepartmentOfficerDTO departmentOfficerDTO) {
        Optional<User> savedOptional = userService.getById(id);
        if (savedOptional.isPresent()) {
            User toUpdate = savedOptional.get();
            User updated = userMapper.updateUserFromDepartmentOfficerDTO(departmentOfficerDTO, toUpdate);
            updated.setDepartment(departmentService.getByName(departmentOfficerDTO.getDepartment()));
            return ResponseEntity.ok(userMapper.toDepartmentOfficer(userService.save(updated)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/department-officers/{id}")
    @PreAuthorize("hasAnyAuthority('DepartmentOfficer_Delete', 'ROLE_Admin')")
    public ResponseEntity<Void> deleteDepartmentOfficer(@PathVariable("id") Long id) {
        userService.deleteDepartmentOfficer(id);
        return ResponseEntity.ok().build();
    }


}
