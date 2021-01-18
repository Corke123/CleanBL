package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.ps.cleanbl.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
