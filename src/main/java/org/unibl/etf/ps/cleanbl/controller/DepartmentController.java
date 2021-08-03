package org.unibl.etf.ps.cleanbl.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.ps.cleanbl.dto.DepartmentDTO;
import org.unibl.etf.ps.cleanbl.mapper.DepartmentMapper;
import org.unibl.etf.ps.cleanbl.service.DepartmentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/departments")
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getDepartmentsDTO() {
        return ResponseEntity.ok(departmentService.getDepartments().stream()
        .map(departmentMapper::toDTO)
        .collect(Collectors.toList()));
    }
}
