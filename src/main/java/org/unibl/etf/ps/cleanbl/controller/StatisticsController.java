package org.unibl.etf.ps.cleanbl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.ps.cleanbl.dto.StatisticsDTO;
import org.unibl.etf.ps.cleanbl.service.StatisticsService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/{year}")
    public ResponseEntity<List<StatisticsDTO>> getStatistics(@PathVariable("year") Integer year) {
        return ResponseEntity.ok(statisticsService.getStatistics(year));
    }
}
