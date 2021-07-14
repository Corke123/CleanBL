package org.unibl.etf.ps.cleanbl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.Role;
import org.unibl.etf.ps.cleanbl.repository.RoleRepository;
import org.unibl.etf.ps.cleanbl.service.RoleService;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private static final String USER_ROLE = "User";
    private static final String DEPARTMENT_OFFICER_ROLE = "DepartmentOfficer";

    private final RoleRepository roleRepository;

    @Override
    public Role getUserRole() {
        return roleRepository.findByName(USER_ROLE)
                .orElseThrow(() -> new RecordNotFoundException("There is no role " + USER_ROLE));
    }


    @Override
    public Role getDepartmentOfficerRole() {
        return roleRepository.findByName(DEPARTMENT_OFFICER_ROLE)
                .orElseThrow(() -> new RecordNotFoundException("There is no role " + DEPARTMENT_OFFICER_ROLE));
    }
}
