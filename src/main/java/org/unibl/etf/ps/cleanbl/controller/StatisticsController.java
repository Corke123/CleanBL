package org.unibl.etf.ps.cleanbl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.StatisticsDTO;
import org.unibl.etf.ps.cleanbl.service.StatisticsService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<List<StatisticsDTO>> getStatistics(@RequestParam Integer year) {
        return ResponseEntity.ok(statisticsService.getStatistics(year));
    }
}
