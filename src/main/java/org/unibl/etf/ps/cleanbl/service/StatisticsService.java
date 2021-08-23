package org.unibl.etf.ps.cleanbl.service;

import org.unibl.etf.ps.cleanbl.dto.StatisticsDTO;

import java.util.List;

public interface StatisticsService {

    List<StatisticsDTO> getStatistics(Integer year);
}
