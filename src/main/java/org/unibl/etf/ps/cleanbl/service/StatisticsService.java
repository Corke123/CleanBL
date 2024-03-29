package org.unibl.etf.ps.cleanbl.service;

import org.unibl.etf.ps.cleanbl.dto.StatisticsDTO;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;

import java.util.List;

public interface StatisticsService {

    List<StatisticsDTO> getYearlyReview(Integer year);
    StatisticsDTO getStatisticsByDepartment(Integer year, DepartmentService departmentService);
}
