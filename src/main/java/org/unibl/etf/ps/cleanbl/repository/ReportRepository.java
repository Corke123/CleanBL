package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.ps.cleanbl.model.Department;
import org.unibl.etf.ps.cleanbl.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findAllByDepartment(Department department, Pageable pageable);
}
