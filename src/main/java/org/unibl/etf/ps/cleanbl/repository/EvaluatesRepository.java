package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unibl.etf.ps.cleanbl.dto.StatisticsPieDTO;
import org.unibl.etf.ps.cleanbl.model.Evaluates;

import java.util.List;

@Repository
public interface EvaluatesRepository extends JpaRepository<Evaluates, Long> {
        @Query(value = "SELECT avg(grade) FROM Evaluates WHERE reportId = :id", nativeQuery = true)
        Double avg(Long id);

        @Query(value = "SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM Evaluates WHERE userId = :userId AND reportId = :reportId")
        boolean existsByReportAndUser(Long reportId, Long userId);

        @Query(value = "select (count(r.id) / (select count(*) from report) * 100) as y, name\n" +
                "from department d left outer join report r\n" +
                "on d.id = r.departmentId and year(createdAt) = :year\n" +
                "group by d.name;", nativeQuery = true)
        List<StatisticsPieDTO> getNumberOfReportsByDepartmentName(Integer year);

        @Query(value = "select rs.name, (count(r.id) / (select count(*) from report) * 100) as y\n" +
                "from reportstatus rs left outer join report r\n" +
                "on r.statusId = rs.id and year(createdAt) = :year\n" +
                "group by rs.name;", nativeQuery = true)
        List<StatisticsPieDTO> getStatisticsByDepartmentName(Integer year);
}
