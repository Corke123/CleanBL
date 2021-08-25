package org.unibl.etf.ps.cleanbl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.YearlyReviewDTO;
import org.unibl.etf.ps.cleanbl.dto.PercentageStatisticsDTO;
import org.unibl.etf.ps.cleanbl.service.StatisticsService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<List<YearlyReviewDTO>> getYearlyReview(@RequestParam Integer year) {
        return ResponseEntity.ok(statisticsService.getYearlyReview(year));
    }

    @GetMapping("/by-department")
    public ResponseEntity<List<PercentageStatisticsDTO>> getPercentageReviewByDepartmentNameAndYear(@RequestParam Integer year) {
        return ResponseEntity.ok(statisticsService.getPercentageReviewByDepartmentNameAndYear(year));
    }

    @GetMapping("/by-report-status")
    public ResponseEntity<List<PercentageStatisticsDTO>> getPercentageReviewByReportStatusAndYear(@RequestParam Integer year) {
        return ResponseEntity.ok(statisticsService.getPercentageReviewByReportStatusAndYear(year));
    }
}
