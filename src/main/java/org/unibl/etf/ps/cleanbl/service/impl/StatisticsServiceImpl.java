package org.unibl.etf.ps.cleanbl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.dto.StatisticsDTO;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;
import org.unibl.etf.ps.cleanbl.model.Evaluates;
import org.unibl.etf.ps.cleanbl.model.ReportStatus;
import org.unibl.etf.ps.cleanbl.repository.EvaluatesRepository;
import org.unibl.etf.ps.cleanbl.repository.ReportRepository;
import org.unibl.etf.ps.cleanbl.service.ReportStatusService;
import org.unibl.etf.ps.cleanbl.service.StatisticsService;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ReportRepository reportRepository;
    private final ReportStatusService reportStatusService;
    private final EvaluatesRepository evaluatesRepository;

    @Override
    public List<StatisticsDTO> getStatistics(Integer year) {
        List<StatisticsDTO> statistics =
                new ArrayList<>(Arrays.asList(new StatisticsDTO(), new StatisticsDTO(), new StatisticsDTO()));

        for(ReportStatus status : reportStatusService.getAllStatuses()) {
            statistics.get(status.getId().intValue() - 1).setName(status.getName());
            statistics.get(status.getId().intValue() - 1).setData(getReportCountByReportStatus(status, year));
        }

        return statistics;
    }


    @Override
    public StatisticsDTO getStatisticsByDepartment(Integer year, DepartmentService departmentService) {
        StatisticsDTO statistics = new StatisticsDTO();

        statistics.setName(departmentService.getName());
        statistics.setData(getEvaluatesAverageByDepartmentService(year, departmentService.getId()));

        return statistics;
    }

    private List<Double> getEvaluatesAverageByDepartmentService(Integer year, Long departmentServiceId) {
        List<Double> values = new ArrayList<>();
        for (Month month : Month.values()) {
            OptionalDouble avg = evaluatesRepository.findAll()
                            .stream()
                            .filter(e -> e.getId().getDepartmentService().getId().equals(departmentServiceId)
                                    && e.getGradedAt().getMonth().equals(month)
                                    && e.getGradedAt().getYear() == year)
                            .mapToDouble(Evaluates::getGrade)
                            .average();
            if (avg.isPresent()) {
                values.add(avg.getAsDouble());
            } else {
                values.add(0.0);
            }
        }
        return  values;
    }

    private List<Double> getReportCountByReportStatus(ReportStatus reportStatus, Integer year) {
        List<Double> values = new ArrayList<>();

        for(Month month : Month.values()) {
            values.add((double) reportRepository.findAll()
                    .stream()
                    .filter(r -> r.getReportStatus().equals(reportStatus)
                            && r.getCreatedAt().getMonth().equals(month)
                            && r.getCreatedAt().getYear() == year)
                    .count());
        }

        return values;
    }
}
