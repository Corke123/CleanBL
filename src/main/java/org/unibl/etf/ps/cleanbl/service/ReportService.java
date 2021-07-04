package org.unibl.etf.ps.cleanbl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.ps.cleanbl.dto.CommentRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportRequest;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.exception.ReportNotFoundException;
import org.unibl.etf.ps.cleanbl.model.Comment;
import org.unibl.etf.ps.cleanbl.model.Department;
import org.unibl.etf.ps.cleanbl.model.Report;
import org.unibl.etf.ps.cleanbl.model.User;
import org.unibl.etf.ps.cleanbl.repository.CommentRepository;
import org.unibl.etf.ps.cleanbl.repository.ReportRepository;
import org.unibl.etf.ps.cleanbl.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {

    private static final String IMAGE_EXTENSION = ".jpg";

    private final ReportRepository reportRepository;
    private final ReportStatusService reportStatusService;
    private final DepartmentService departmentService;
    private final PartOfTheCityService partOfTheCityService;
    private final StreetService streetService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public Report saveReport(ReportRequest reportRequest) {
        log.info("Saving new report");

        Report report = Report.builder()
                .user(userService.getUserByUsername(userService.getCurrentlyLoggedInUser().getUsername()))
                .description(reportRequest.getDescription())
                .imagePath(UUID.randomUUID() + IMAGE_EXTENSION)
                .reportStatus(reportStatusService.getSentStatus())
                .street(streetService.getByNameAndPartOfTheCity(reportRequest.getStreet(),
                        partOfTheCityService.getPartOfTheCityByName(reportRequest.getPartOfTheCity())))
                .department(departmentService.getByName(reportRequest.getDepartmentName()))
                .build();

        Report saved = reportRepository.save(report);

        saveImage(report.getImagePath(), reportRequest.getBase64Image());

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

    public Page<Report> getAllReports(PageRequest pageRequest) {
        log.info("Getting all reports by page: " + pageRequest.getPageNumber());
        return reportRepository.findAll(pageRequest);
    }

    public Page<Report> getReportsForDepartmentOfficer(PageRequest pageRequest) {
        Department department = userService.getUserByUsername(userService.getCurrentlyLoggedInUser().getUsername())
                .getDepartment();
        log.info("Getting all reports for department: " + department.getName() + " by page = " + pageRequest.getPageNumber() +
                " and size = " + pageRequest.getPageSize());
        return reportRepository.findAllByDepartment(department, pageRequest);
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
        report.setDescription(reportRequest.getDescription());
        report.setStreet(streetService.getByNameAndPartOfTheCity(reportRequest.getStreet(),
                partOfTheCityService.getPartOfTheCityByName(reportRequest.getPartOfTheCity())));

        reportRepository.save(report);
    }

    public Report modifyDepartment(Report report, Department department) {
        log.info("Set department " + department.getName() + " to report with id: " + report.getId());
        return reportRepository.save(report.toBuilder().department(department).build());
    }

    public Report approveReport(Report report) {
        log.info("Approve report with id: " + report.getId());
        return reportRepository.save(report.toBuilder()
                .reportStatus(reportStatusService.getCompletedStatus())
                .valid(true)
                .processed(LocalDate.now())
                .build());
    }

    public Report rejectReport(Report report) {
        log.info("Reject report with id: " + report.getId());
        return reportRepository.save(report.toBuilder()
                .reportStatus(reportStatusService.getCompletedStatus())
                .valid(false)
                .processed(LocalDate.now())
                .build());
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

    public Report setDepartmentServiceAndMoveToInProcess(Report report,
                                                         org.unibl.etf.ps.cleanbl.model.DepartmentService departmentService) {
        log.info("Set department service with name: " + departmentService.getName()
                + " to report with id: " + report.getId());
        return reportRepository.save(report.toBuilder().departmentService(departmentService)
                .reportStatus(reportStatusService.getInProcessStatus()).build());
    }
}
