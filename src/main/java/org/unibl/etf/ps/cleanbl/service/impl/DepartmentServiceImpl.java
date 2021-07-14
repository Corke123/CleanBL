package org.unibl.etf.ps.cleanbl.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.Department;
import org.unibl.etf.ps.cleanbl.repository.DepartmentRepository;
import org.unibl.etf.ps.cleanbl.service.DepartmentService;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> getDepartments() {
        log.info("Getting all departments");
        return departmentRepository.findAll();
    }

    @Override
    public Department getByName(String name) {
        log.info("Find department with name: " + name);
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException("Unable to find department with name: " + name));
    }

}
