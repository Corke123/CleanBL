package org.unibl.etf.ps.cleanbl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.dto.StatisticsDTO;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;
import org.unibl.etf.ps.cleanbl.model.Evaluates;
import org.unibl.etf.ps.cleanbl.repository.EvaluatesRepository;
import org.unibl.etf.ps.cleanbl.repository.ReportRepository;
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
    private final EvaluatesRepository evaluatesRepository;

    @Override
    public List<StatisticsDTO> getYearlyReview(Integer year) {

        return new ArrayList<>(Arrays.asList(
                new StatisticsDTO("Poslan", getReportCountByReportStatusSent(year)),
                new StatisticsDTO("U procesu", getReportCountByReportStatusInProcess(year)),
                new StatisticsDTO("Završen", getReportCountByReportStatusCompleted(year))));
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

    private List<Double> getReportCountByReportStatusSent(Integer year) {
        List<Double> values = new ArrayList<>();

        for(Month month : Month.values()) {
            values.add((double) reportRepository.findAll()
                    .stream()
                    .filter(r -> r.getCreatedAt() != null
                            && r.getCreatedAt().getMonth().equals(month)
                            && r.getCreatedAt().getYear() == year)
                    .count());
        }

        return values;
    }

    private List<Double> getReportCountByReportStatusCompleted(Integer year) {
        List<Double> values = new ArrayList<>();

        for(Month month : Month.values()) {
            values.add((double) reportRepository.findAll()
                    .stream()
                    .filter(r -> r.getProcessed() != null
                            && r.getProcessed().getMonth().equals(month)
                            && r.getProcessed().getYear() == year)
                    .count());
        }

        return values;
    }

    private List<Double> getReportCountByReportStatusInProcess(Integer year) {
        List<Double> values = new ArrayList<>();

        for(Month month : Month.values()) {
            values.add((double) reportRepository.findAll()
                    .stream()
                    .filter(r -> r.getMovedToInProcess() != null
                            && r.getMovedToInProcess().getMonth().equals(month)
                            && r.getMovedToInProcess().getYear() == year)
                    .count());
        }

        return values;
    }

}
