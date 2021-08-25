package org.unibl.etf.ps.cleanbl.service;

import org.unibl.etf.ps.cleanbl.dto.StatisticsDTO;
import org.unibl.etf.ps.cleanbl.dto.StatisticsPieDTO;

import java.util.List;

public interface StatisticsService {

    List<StatisticsDTO> getStatistics(Integer year);
    List<StatisticsPieDTO> getStatisticsForNumberOfReportsByDepartmentName(Integer year);
    List<StatisticsPieDTO> getStatisticsByDepartmentName(Integer year);
}
