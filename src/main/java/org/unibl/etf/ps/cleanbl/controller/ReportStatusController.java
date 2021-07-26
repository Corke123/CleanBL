package org.unibl.etf.ps.cleanbl.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.ps.cleanbl.model.ReportStatus;
import org.unibl.etf.ps.cleanbl.service.ReportStatusService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report-statuses")
@AllArgsConstructor
public class ReportStatusController {
    private final ReportStatusService reportStatusService;

    @GetMapping
    public ResponseEntity<List<ReportStatus>> getReportServices() {
        return ResponseEntity.ok(reportStatusService.getAllStatuses());
    }
}
