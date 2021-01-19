package org.unibl.etf.ps.cleanbl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.ReportRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportResponse;
import org.unibl.etf.ps.cleanbl.exception.ReportNotFoundException;
import org.unibl.etf.ps.cleanbl.service.ReportService;

import javax.validation.Valid;
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

    @PostMapping
    public ResponseEntity<ReportResponse> uploadReport(@RequestBody ReportRequest reportRequest) {
        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.saveReport(reportRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(reportService.getReport(id));
        } catch (ReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> deleteReport(@PathVariable("id") Long id) {
        try {
            if (reportService.deleteReport(id)) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (ReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> deletePerson(@PathVariable("id") Long id, @Valid @RequestBody ReportRequest reportRequest) {
        try {
            if (reportService.updateReport(id, reportRequest)) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (ReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
