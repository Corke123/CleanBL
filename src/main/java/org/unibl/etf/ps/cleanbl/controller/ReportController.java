package org.unibl.etf.ps.cleanbl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.CommentDTO;
import org.unibl.etf.ps.cleanbl.dto.CommentRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportResponse;
import org.unibl.etf.ps.cleanbl.mapper.CommentMapper;
import org.unibl.etf.ps.cleanbl.mapper.ReportMapper;
import org.unibl.etf.ps.cleanbl.model.Comment;
import org.unibl.etf.ps.cleanbl.model.Report;
import org.unibl.etf.ps.cleanbl.service.ReportService;
import org.unibl.etf.ps.cleanbl.service.UserService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final String PAGE = "1";
    private static final int SIZE = 12;

    private final ReportService reportService;
    private final ReportMapper reportMapper;
    private final CommentMapper commentMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<ReportResponse>> getAllReports(@RequestParam(value = "page", defaultValue = PAGE) Integer page) {
        return ResponseEntity.ok(reportService.getAllReports(PageRequest.of(page, SIZE))
                .map(this::createReportResponseFromReport));
    }

    @PostMapping
    public ResponseEntity<ReportResponse> uploadReport(@Valid @RequestBody ReportRequest reportRequest) {
        Report savedReport = reportService.saveReport(reportRequest);
        return ResponseEntity.created(URI.create("/api/v1/reports/" + savedReport.getId()))
                .body(createReportResponseFromReport(savedReport));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable Long id) {
        return reportService.getReport(id)
                .map(report -> ResponseEntity.ok(createReportResponseFromReport(report)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteReport(@PathVariable("id") Long id) {
        return reportService.getReport(id)
                .map(report -> {
                    if (userService.isLoggedInUserDepartmentOfficer() || report.canUserEditReport(userService.getCurrentlyLoggedInUser())) {
                        reportService.deleteReport(report);
                        return ResponseEntity.ok().build();
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Object> updateReport(@PathVariable("id") Long id, @Valid @RequestBody ReportRequest reportRequest) {
        return reportService.getReport(id)
                .map(report -> {
                    if (userService.isLoggedInUserDepartmentOfficer() || report.canUserEditReport(userService.getCurrentlyLoggedInUser())) {
                        reportService.updateReport(report, reportRequest);
                        return ResponseEntity.ok().build();
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{reportId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable("reportId") Long reportId) {
        return ResponseEntity.ok(reportService.getCommentsForReport(reportId).stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{reportId}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable("reportId") Long reportId, @RequestBody @Valid CommentRequest commentRequest) {
        Comment saved = reportService.addComment(reportId, commentRequest);
        return ResponseEntity.created(URI.create("/api/v1/reports/" + reportId + "/comments/" + saved.getId()))
                .body(commentMapper.toDTO(saved));
    }

    private ReportResponse createReportResponseFromReport(Report report) {
        ReportResponse response = reportMapper.reportToReportResponse(report);
        response.setBase64Image(report.encodeImage(uploadDir));
        return response;
    }
}
