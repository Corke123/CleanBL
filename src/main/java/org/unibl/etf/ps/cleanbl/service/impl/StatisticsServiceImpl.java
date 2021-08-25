package org.unibl.etf.ps.cleanbl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.dto.YearlyReviewDTO;
import org.unibl.etf.ps.cleanbl.dto.PercentageStatisticsDTO;
import org.unibl.etf.ps.cleanbl.model.ReportStatus;
import org.unibl.etf.ps.cleanbl.repository.EvaluatesRepository;
import org.unibl.etf.ps.cleanbl.repository.ReportRepository;
import org.unibl.etf.ps.cleanbl.service.ReportStatusService;
import org.unibl.etf.ps.cleanbl.service.StatisticsService;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final EvaluatesRepository evaluatesRepository;
    private final ReportRepository reportRepository;
    private final ReportStatusService reportStatusService;

    @Override
    public List<YearlyReviewDTO> getYearlyReview(Integer year) {
        List<YearlyReviewDTO> statistics =
                new ArrayList<>(Arrays.asList(new YearlyReviewDTO(), new YearlyReviewDTO(), new YearlyReviewDTO()));

        for(ReportStatus status : reportStatusService.getAllStatuses()) {
            statistics.get(status.getId().intValue() - 1).setName(status.getName());
            statistics.get(status.getId().intValue() - 1).setData(getReportCountByReportStatus(status, year));
        }

        return statistics;
    }

    @Override
    public List<PercentageStatisticsDTO> getPercentageReviewByDepartmentNameAndYear(Integer year) {
        log.info("Get list of percentage statistics by year, for every month: " + year);
        return evaluatesRepository.getPercentageReviewByDepartmentNameAndYear(year);
    }

    @Override
    public List<PercentageStatisticsDTO> getPercentageReviewByReportStatusAndYear(Integer year) {
        log.info("Get list of percentage statistics by year: " + year);
        return evaluatesRepository.getPercentageReviewByReportStatusAndYear(year);
    }


    private List<Integer> getReportCountByReportStatus(ReportStatus reportStatus, Integer year) {
        log.info("Get list of percentage statistics by year: " + year);
        List<Integer> values = new ArrayList<>();

        for(Month month : Month.values()) {
            values.add((int) reportRepository.findAll()
                    .stream()
                    .filter(r -> r.getReportStatus().equals(reportStatus)
                            && r.getCreatedAt().getMonth().equals(month)
                            && r.getCreatedAt().getYear() == year)
                    .count());
        }

        return values;
    }
}
