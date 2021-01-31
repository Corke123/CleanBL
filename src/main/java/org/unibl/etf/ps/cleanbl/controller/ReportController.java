package org.unibl.etf.ps.cleanbl.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.CommentDTO;
import org.unibl.etf.ps.cleanbl.dto.CommentRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportResponse;
import org.unibl.etf.ps.cleanbl.exception.ReportNotFoundException;
import org.unibl.etf.ps.cleanbl.service.ReportService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/reports")
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public List<ReportResponse> getAllReports() {
        return reportService.getAllReports();
    }

    @PostMapping
    public ReportResponse uploadReport(@Valid @RequestBody ReportRequest reportRequest) {
        return  reportService.saveReport(reportRequest);
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

    @DeleteMapping(path = "/{id}")
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

    @PutMapping(path = "/{id}")
    public ResponseEntity<Object> updateReport(@PathVariable("id") Long id, @Valid @RequestBody ReportRequest reportRequest) {
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

    @GetMapping("/{reportId}/comments/")
    public List<CommentDTO> getComments(@PathVariable("reportId") Long reportId) {
        return reportService.getCommentsForReport(reportId);
    }

    @PostMapping("/{reportId}/comments/")
    public CommentDTO addComment(@PathVariable("reportId") Long reportId, @RequestBody @Valid CommentRequest commentRequest) {
        return reportService.addComment(reportId, commentRequest);
    }
}
