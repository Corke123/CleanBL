package org.unibl.etf.ps.cleanbl.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;
import org.unibl.etf.ps.cleanbl.repository.DepartmentServiceRepository;
import org.unibl.etf.ps.cleanbl.service.DepartmentServiceService;
import org.unibl.etf.ps.cleanbl.service.UserService;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DepartmentServiceServiceImpl implements DepartmentServiceService {
    private final UserService userService;

    private final DepartmentServiceRepository departmentServiceRepository;

    @Override
    public List<DepartmentService> getDepartmentServicesForUsersDepartment() {
        return departmentServiceRepository.findAllByDepartmentName(
                userService.getUserByUsername(userService.getCurrentlyLoggedInUser().getUsername())
                        .getDepartment().getName());
    }

    @Override
    public DepartmentService getByName(String name) {
        log.info("Get department service by name: " + name);
        return departmentServiceRepository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException("Unable to find department service with name: " + name));
    }
}
