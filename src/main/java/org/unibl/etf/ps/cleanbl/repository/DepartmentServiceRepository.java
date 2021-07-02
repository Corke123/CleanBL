package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;

import java.util.List;

public interface DepartmentServiceRepository extends JpaRepository<DepartmentService, Long> {
    List<DepartmentService> findAllByDepartmentName(String departmentName);
}
