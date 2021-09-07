package org.unibl.etf.ps.cleanbl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.StatisticsDTO;
import org.unibl.etf.ps.cleanbl.repository.DepartmentServiceRepository;
import org.unibl.etf.ps.cleanbl.service.StatisticsService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final DepartmentServiceRepository departmentServiceRepository;

    @GetMapping
    public ResponseEntity<List<StatisticsDTO>> getYearlyReview(@RequestParam Integer year) {
        return ResponseEntity.ok(statisticsService.getYearlyReview(year));
    }

    @GetMapping("/by-department-service")
    public ResponseEntity<StatisticsDTO> getStatisticsByDepartment(@RequestParam Integer year,
                                                                   @RequestParam String departmentServiceName) {
       return departmentServiceRepository.findByName(departmentServiceName)
               .map(departmentService -> ResponseEntity.ok(statisticsService.getStatisticsByDepartment(year, departmentService)))
               .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
