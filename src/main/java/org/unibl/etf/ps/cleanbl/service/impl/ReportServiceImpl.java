package org.unibl.etf.ps.cleanbl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.ps.cleanbl.dto.*;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.exception.ReportNotFoundException;
import org.unibl.etf.ps.cleanbl.model.*;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;
import org.unibl.etf.ps.cleanbl.repository.*;
import org.unibl.etf.ps.cleanbl.service.*;
import org.unibl.etf.ps.cleanbl.service.DepartmentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final String IMAGE_EXTENSION = ".jpg";
    private static final String TITLE = "title";
    private static final String TEXT = "text";
    private static final String REQUEST_LINK = "requestLink";
    private static final String REPORT_LINK_USER = "http://localhost:4200/reports/";
    private static final String REPORT_LINK_DEPARTMENT_OFFICER = "http://localhost:4200/management/active-reports/";
    private static final String RECEIVE_MESSAGE = "Stigao je novi zahtjev, da pregledate kliknite na sledeći link ";
    private static final String APPROVE_MESSAGE = "Vaš zahtjev je sad uspješno završen, da pregledate kliknite na sledeći link ";
    private static final String IN_PROCESS_MESSAGE = "Vaš zahtjev je u procesu, da pregledate klinite na sledeći link ";
    private static final String MODIFY_MESSAGE = "Vaš zahtjev je modifikovan, da pregledate kliknite na sledeći link ";
    private static final String REJECT_MESSAGE = "Vaš zahtjev je odbijen, da pregledate klinite na sledeći link ";
    private static final String EMAIL_SUBJECT = "CleanBL obavještenje";

    private final ReportRepository reportRepository;
    private final ReportCriteriaRepository reportCriteriaRepository;
    private final ReportStatusService reportStatusService;
    private final DepartmentService departmentService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EvaluatesRepository evaluatesRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    @Transactional
    public Report saveReport(ReportRequest reportRequest) {
        log.info("Saving new report");

        Report report = Report.builder()
                .user(userService.getUserByUsername(userService.getCurrentlyLoggedInUser().getUsername()))
                .title(reportRequest.getTitle())
                .description(reportRequest.getDescription())
                .imagePath(UUID.randomUUID() + IMAGE_EXTENSION)
                .reportStatus(reportStatusService.getSentStatus())
                .latitude(reportRequest.getLatitude())
                .longitude(reportRequest.getLongitude())
                .department(departmentService.getByName(reportRequest.getDepartmentName()))
                .build();

        Report saved = reportRepository.save(report);

        saveImage(report.getImagePath(), reportRequest.getBase64Image());

        sendMail(RECEIVE_MESSAGE, REPORT_LINK_DEPARTMENT_OFFICER + report.getId(), report.getDepartment().getEmail());

        return saved;
    }

    private void saveImage(String imageName, String base64Image) {
        Path path = Paths.get(uploadDir + imageName);
        try {
            Files.write(path, Base64.getDecoder().decode(base64Image));
            log.debug("Image saved to: " + uploadDir + imageName);
        } catch (IOException e) {
            log.info("Unable to save image on path: " + uploadDir + imageName);
        }
    }

    public Page<Report> getAllReports(ReportPage reportPage, ReportSearchCriteria reportSearchCriteria) {
        log.info("Get all page filtered by: " + reportPage.getPageNumber() + " size of page: " + reportPage.getPageSize() + " ordered by: "
                + reportPage.getSortBy() + " ASC/DESC: " + reportPage.getSortDirection() + " by filters: " + reportSearchCriteria.getStatus()
                + ", " + reportSearchCriteria.getTitle() + " and " + reportSearchCriteria.getUsername());
        return reportCriteriaRepository.findAllWithFilters(reportPage, reportSearchCriteria);
    }

    public Page<Report> getReportsForDepartmentOfficer(ReportPage reportPage, ReportSearchCriteria reportSearchCriteria) {
        log.info("Get all page filtered by: " + reportPage.getPageNumber() + " size of page: " + reportPage.getPageSize() + " by filters: " + reportSearchCriteria.getStatus()
                + ", " + reportSearchCriteria.getTitle() + " and " + reportSearchCriteria.getUsername());
        Department department = userService.getUserByUsername(userService.getCurrentlyLoggedInUser().getUsername())
                .getDepartment();
        return reportCriteriaRepository.findAllWithFiltersForDepartmentOfficersReports(reportPage, reportSearchCriteria, department);
    }

    public Optional<Report> getReport(Long id) throws ReportNotFoundException {
        log.info("Getting report with id: " + id);
        return reportRepository.findById(id);
    }

    public void deleteReport(Report report) throws ReportNotFoundException {
        log.info("Delete report with id: " + report.getId());
        deleteImage(report.getImagePath());
        reportRepository.delete(report);
    }

    private void deleteImage(String image) {
        try {
            Files.delete(Path.of(uploadDir + image));
            log.info("Deleting file: + " + image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void updateReport(Report report, ReportRequest reportRequest) throws ReportNotFoundException {
        log.info("Updating report with id: " + report.getId());
        report.setDepartment(departmentService.getByName(reportRequest.getDepartmentName()));
        report.setTitle(reportRequest.getTitle());
        report.setDescription(reportRequest.getDescription());
        report.setLatitude(reportRequest.getLatitude());
        report.setLongitude(reportRequest.getLongitude());

        sendMail(MODIFY_MESSAGE, REPORT_LINK_USER + report.getId(), report.getUser().getEmail());
        sendMail(RECEIVE_MESSAGE, REPORT_LINK_USER + report.getId(), report.getDepartmentService().getEmail());

        reportRepository.save(report);
    }

    public Report modifyDepartment(Report report, Department department) {
        log.info("Set department " + department.getName() + " to report with id: " + report.getId());
        Report modifiedReport = reportRepository.save(report.toBuilder().department(department).build());

        sendMail(MODIFY_MESSAGE, REPORT_LINK_USER + report.getId(), report.getUser().getEmail());
        sendMail(RECEIVE_MESSAGE, REPORT_LINK_DEPARTMENT_OFFICER + report.getId(), report.getDepartment().getEmail());

        return modifiedReport;
    }

    public Report approveReport(Report report) {
        log.info("Approve report with id: " + report.getId());
        Report approvedReport = reportRepository.save(report.toBuilder()
                .reportStatus(reportStatusService.getCompletedStatus())
                .valid(true)
                .processed(LocalDateTime.now())
                .build());

        sendMail(APPROVE_MESSAGE, REPORT_LINK_USER + report.getId(), report.getUser().getEmail());

        return approvedReport;
    }

    public Report rejectReport(Report report) {
        log.info("Reject report with id: " + report.getId());
        Report rejectedReport = reportRepository.save(report.toBuilder()
                .reportStatus(reportStatusService.getCompletedStatus())
                .valid(false)
                .processed(LocalDateTime.now())
                .build());

        sendMail(REJECT_MESSAGE, REPORT_LINK_USER + report.getId(), report.getUser().getEmail());

        return rejectedReport;
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsForReport(Long reportId) throws ReportNotFoundException {
        log.info("Getting comments for report with id: " + reportId);
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new ReportNotFoundException("There is no report with id: " + reportId));
        return commentRepository.findByReport(report);
    }

    @Transactional
    public Comment addComment(Long reportId, CommentRequest commentRequest) throws ReportNotFoundException {
        log.info("Adding new comment for report with id: " + reportId);
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException("There is " + "no report with id: " + reportId));
        String username = userService.getCurrentlyLoggedInUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("There is no user with username: " + username));
        Comment comment = Comment.builder()
                .report(report)
                .content(commentRequest.getContent())
                .user(user)
                .build();
        return commentRepository.save(comment);
    }

    @Transactional
    public Double rateReport(Report report, EvaluatesRequest evaluatesRequest) throws ReportNotFoundException {
        log.info("Adding new rating " + evaluatesRequest.getGrade() + " for report with id: " + report.getId());
        String username = userService.getCurrentlyLoggedInUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("There is no user with username: " + username));
        EvaluatesId evaluatesId = EvaluatesId.builder()
                .report(report).user(user).departmentService(report.getDepartmentService()).build();
        Evaluates evaluates = Evaluates.builder().id(evaluatesId).grade(evaluatesRequest.getGrade()).build();
        evaluatesRepository.save(evaluates);

        return evaluatesRepository.avg(report.getId());
    }

    public Report setDepartmentServiceAndMoveToInProcess(Report report,
                                                         org.unibl.etf.ps.cleanbl.model.DepartmentService departmentService) {
        log.info("Set department service with name: " + departmentService.getName()
                + " to report with id: " + report.getId());
        Report inProcessReport = reportRepository.save(report.toBuilder().departmentService(departmentService)
                .reportStatus(reportStatusService.getInProcessStatus()).build());

        sendMail(IN_PROCESS_MESSAGE, REPORT_LINK_USER + report.getId(), report.getUser().getEmail());
        sendMail(RECEIVE_MESSAGE, REPORT_LINK_USER + report.getId(), report.getDepartmentService().getEmail());

        return inProcessReport;
    }

    private void sendMail(String message, String requestLink, String email) {
        Map<String, Object> parameters = new HashMap<> ();
        parameters.put(TITLE, ReportServiceImpl.EMAIL_SUBJECT);
        parameters.put(TEXT, message);
        parameters.put(REQUEST_LINK, requestLink);

        emailService.sendMessageUsingThymeleafTemplate(email, ReportServiceImpl.EMAIL_SUBJECT, parameters);
    }
}
