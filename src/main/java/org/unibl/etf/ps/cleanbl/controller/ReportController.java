package org.unibl.etf.ps.cleanbl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.*;
import org.unibl.etf.ps.cleanbl.mapper.CommentMapper;
import org.unibl.etf.ps.cleanbl.mapper.ReportMapper;
import org.unibl.etf.ps.cleanbl.model.Comment;
import org.unibl.etf.ps.cleanbl.model.Report;
import org.unibl.etf.ps.cleanbl.service.DepartmentService;
import org.unibl.etf.ps.cleanbl.service.DepartmentServiceService;
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

    private static final String PAGE = "0";
    private static final String SIZE = "12";

    private final ReportService reportService;
    private final ReportMapper reportMapper;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final DepartmentServiceService departmentServiceService;

    @GetMapping
    public ResponseEntity<Page<ReportResponse>> getAllReports(ReportPage reportPage, ReportSearchCriteria reportSearchCriteria) {
        return  ResponseEntity.ok(reportService.getAllReports(reportPage,
                reportSearchCriteria).map(this::createReportResponseFromReport));
    }

    @GetMapping("/department-officer")
    @PreAuthorize("hasRole('ROLE_DepartmentOfficer')")
    public ResponseEntity<Page<ReportResponse>> getDepartmentOfficersReports(ReportPage reportPage, ReportSearchCriteria reportSearchCriteria) {
        return ResponseEntity.ok(reportService.getReportsForDepartmentOfficer(reportPage,
                reportSearchCriteria).map(this::createReportResponseFromReport));
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

    @PatchMapping("/{id}/department")
    @PreAuthorize("hasRole('ROLE_DepartmentOfficer')")
    public ResponseEntity<ReportResponse> updateDepartment(
            @PathVariable("id") Long id,
            @Valid @RequestBody DepartmentDTO departmentDTO) {
        return reportService.getReport(id)
                .map(report -> ResponseEntity.ok(
                        this.createReportResponseFromReport(
                                reportService.modifyDepartment(
                                        report,
                                        departmentService.getByName(departmentDTO.getName())))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/department-service")
    @PreAuthorize("hasRole('ROLE_DepartmentOfficer')")
    public ResponseEntity<ReportResponse> updateDepartmentService(
            @PathVariable("id") Long id,
            @Valid @RequestBody DepartmentServiceDTO departmentServiceDTO) {
        return reportService.getReport(id)
                .map(report -> ResponseEntity.ok(
                        this.createReportResponseFromReport(
                                reportService.setDepartmentServiceAndMoveToInProcess(
                                        report,
                                        departmentServiceService.getByName(departmentServiceDTO.getDepartmentService())))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ROLE_DepartmentOfficer')")
    public ResponseEntity<ReportResponse> approveReport(@PathVariable("id") Long id) {
        return reportService.getReport(id)
                .map(report -> ResponseEntity.ok(this.createReportResponseFromReport(reportService.approveReport(report))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ROLE_DepartmentOfficer')")
    public ResponseEntity<ReportResponse> rejectReport(@PathVariable("id") Long id) {
        return reportService.getReport(id)
                .map(report -> ResponseEntity.ok(this.createReportResponseFromReport(reportService.rejectReport(report))))
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

    @PostMapping("/{reportId}/rating")
    public ResponseEntity<Double> rateReport(@PathVariable("reportId") Long reportId, @RequestBody @Valid EvaluatesRequest evaluatesRequest) {

        return reportService.getReport(reportId)
                .map(report -> ResponseEntity.ok(reportService.rateReport(report, evaluatesRequest)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ReportResponse createReportResponseFromReport(Report report) {
        ReportResponse response = reportMapper.reportToReportResponse(report);
        response.setBase64Image(report.encodeImage(uploadDir));
        return response;
    }
}
