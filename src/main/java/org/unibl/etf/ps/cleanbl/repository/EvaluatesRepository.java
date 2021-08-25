package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unibl.etf.ps.cleanbl.dto.PercentageStatisticsDTO;
import org.unibl.etf.ps.cleanbl.model.Evaluates;

import java.util.List;

@Repository
public interface EvaluatesRepository extends JpaRepository<Evaluates, Long> {
        @Query(value = "SELECT avg(grade) FROM Evaluates WHERE reportId = :id", nativeQuery = true)
        Double avg(Long id);

        @Query(value = "SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM Evaluates WHERE userId = :userId AND reportId = :reportId")
        boolean existsByReportAndUser(Long reportId, Long userId);

        @Query(value = "SELECT (COUNT(r.id) / (SELECT COUNT(*) FROM report) * 100) AS y, name\n" +
                "FROM department d LEFT OUTER JOIN report r\n" +
                "ON d.id = r.departmentId AND YEAR(createdAt) = :year\n" +
                "GROUP BY d.name;", nativeQuery = true)
        List<PercentageStatisticsDTO> getPercentageReviewByDepartmentNameAndYear(Integer year);

        @Query(value = "SELECT rs.name, (COUNT(r.id) / (SELECT COUNT(*) FROM report) * 100) AS y\n" +
                "FROM reportstatus rs LEFT OUTER JOIN report r\n" +
                "ON r.statusId = rs.id AND YEAR(createdAt) = :year\n" +
                "GROUP BY rs.name;", nativeQuery = true)
        List<PercentageStatisticsDTO> getPercentageReviewByReportStatusAndYear(Integer year);
}
