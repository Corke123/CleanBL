package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;

import java.util.List;
import java.util.Optional;

public interface DepartmentServiceRepository extends JpaRepository<DepartmentService, Long> {
    List<DepartmentService> findAllByDepartmentName(String departmentName);

    Optional<DepartmentService> findByName(String name);
}
