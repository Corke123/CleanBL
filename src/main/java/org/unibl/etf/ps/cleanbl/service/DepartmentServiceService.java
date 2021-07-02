package org.unibl.etf.ps.cleanbl.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;
import org.unibl.etf.ps.cleanbl.repository.DepartmentServiceRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DepartmentServiceService {
    private final UserService userService;

    private final DepartmentServiceRepository departmentServiceRepository;

    public List<DepartmentService> getDepartmentServicesForUsersDepartment() {
        return departmentServiceRepository.findAllByDepartmentName(
                userService.getUserByUsername(userService.getCurrentlyLoggedInUser().getUsername())
                        .getDepartment().getName());
    }
}
