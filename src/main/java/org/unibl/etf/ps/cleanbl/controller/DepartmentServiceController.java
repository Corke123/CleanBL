package org.unibl.etf.ps.cleanbl.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;
import org.unibl.etf.ps.cleanbl.service.DepartmentServiceService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department-services")
@AllArgsConstructor
public class DepartmentServiceController {
    private final DepartmentServiceService departmentServiceService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_DepartmentOfficer')")
    public ResponseEntity<List<DepartmentService>> getDepartmentServices() {
        return ResponseEntity.ok(departmentServiceService.getDepartmentServicesForUsersDepartment());
    }
}

