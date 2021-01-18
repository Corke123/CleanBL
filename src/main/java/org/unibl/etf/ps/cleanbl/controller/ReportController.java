package org.unibl.etf.ps.cleanbl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.ReportRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportResponse;
import org.unibl.etf.ps.cleanbl.service.ReportService;

import java.util.List;

@RestController
@RequestMapping(path = "api/reports/")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getAllReports() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reportService.getAllReports());
    }

    @PostMapping(path = "/upload")
    public ResponseEntity<ReportResponse> uploadReport(@RequestBody ReportRequest reportRequest) {
        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.saveReport(reportRequest));
    }
}
