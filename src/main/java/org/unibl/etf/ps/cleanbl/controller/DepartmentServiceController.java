package org.unibl.etf.ps.cleanbl.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.ps.cleanbl.dto.DepartmentServiceDTO;
import org.unibl.etf.ps.cleanbl.mapper.DepartmentServiceMapper;
import org.unibl.etf.ps.cleanbl.service.DepartmentServiceService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/department-services")
@AllArgsConstructor
public class DepartmentServiceController {
    private final DepartmentServiceService departmentServiceService;
    private final DepartmentServiceMapper departmentServiceMapper;

    @GetMapping
    public ResponseEntity<List<DepartmentServiceDTO>> getDepartmentsServiceDTO() {
        return ResponseEntity.ok(departmentServiceService.getDepartmentServicesForUsersDepartment().stream()
                .map(departmentServiceMapper::toDTO)
                .collect(Collectors.toList()));
    }
}

