package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unibl.etf.ps.cleanbl.model.Evaluates;

@Repository
public interface EvaluatesRepository extends JpaRepository<Evaluates, Long> {
        @Query(value = "SELECT avg(grade) FROM Evaluates WHERE reportId = :id", nativeQuery = true)
        Double avg(Long id);

        @Query(value = "SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM Evaluates WHERE userId = :userId AND reportId = :reportId")
        boolean existsByReportAndUser(Long reportId, Long userId);
}
