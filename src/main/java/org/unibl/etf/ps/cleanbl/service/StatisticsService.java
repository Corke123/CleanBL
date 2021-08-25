package org.unibl.etf.ps.cleanbl.service;

import org.unibl.etf.ps.cleanbl.dto.YearlyReviewDTO;
import org.unibl.etf.ps.cleanbl.dto.PercentageStatisticsDTO;

import java.util.List;

public interface StatisticsService {

    List<YearlyReviewDTO> getYearlyReview(Integer year);
    List<PercentageStatisticsDTO> getPercentageReviewByDepartmentNameAndYear(Integer year);
    List<PercentageStatisticsDTO> getPercentageReviewByReportStatusAndYear(Integer year);
}
