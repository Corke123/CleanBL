package org.unibl.etf.ps.cleanbl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.unibl.etf.ps.cleanbl.dto.*;
import org.unibl.etf.ps.cleanbl.exception.ReportNotFoundException;
import org.unibl.etf.ps.cleanbl.model.*;

import java.util.List;
import java.util.Optional;

public interface ReportService {

    Report saveReport(ReportRequest reportRequest);
    Page<Report> getAllReports(ReportPage reportPage, ReportSearchCriteria reportSearchCriteria);
    Page<Report> getReportsForDepartmentOfficer(PageRequest pageRequest);
    Optional<Report> getReport(Long id) throws ReportNotFoundException;
    void deleteReport(Report report) throws ReportNotFoundException;
    void updateReport(Report report, ReportRequest reportRequest) throws ReportNotFoundException;
    Report modifyDepartment(Report report, Department department);
    Report approveReport(Report report);
    Report rejectReport(Report report);
    List<Comment> getCommentsForReport(Long reportId) throws ReportNotFoundException;
    Comment addComment(Long reportId, CommentRequest commentRequest) throws ReportNotFoundException;
    Report setDepartmentServiceAndMoveToInProcess(Report report,
                                                  org.unibl.etf.ps.cleanbl.model.DepartmentService departmentService);
    Double rateReport(Report report, EvaluatesRequest evaluatesRequest);
}
