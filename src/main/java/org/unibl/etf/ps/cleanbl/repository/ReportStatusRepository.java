package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.ps.cleanbl.model.ReportStatus;

import java.util.Optional;

@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, Long> {
    Optional<ReportStatus> findByName(String name);
}
