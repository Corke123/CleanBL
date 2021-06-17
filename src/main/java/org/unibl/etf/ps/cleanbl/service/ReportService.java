package org.unibl.etf.ps.cleanbl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.ps.cleanbl.dto.CommentRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportResponse;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.exception.ReportNotFoundException;
import org.unibl.etf.ps.cleanbl.mapper.ReportMapper;
import org.unibl.etf.ps.cleanbl.model.Comment;
import org.unibl.etf.ps.cleanbl.model.Report;
import org.unibl.etf.ps.cleanbl.model.User;
import org.unibl.etf.ps.cleanbl.repository.CommentRepository;
import org.unibl.etf.ps.cleanbl.repository.ReportRepository;
import org.unibl.etf.ps.cleanbl.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final String IMAGE_EXTENSION = ".jpg";

    private final ReportRepository reportRepository;
    private final ReportStatusService reportStatusService;
    private final DepartmentService departmentService;
    private final PartOfTheCityService partOfTheCityService;
    private final StreetService streetService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ReportMapper reportMapper;

    @Transactional
    public Report saveReport(ReportRequest reportRequest) {
        log.info("Saving new report");

        Report report = Report.builder()
                .user(userService.getEndUserByUsername(userService.getCurrentlyLoggedInUser().getUsername()))
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

    public List<Report> getAllReports() {
        log.info("Getting all reports");
        return reportRepository.findAll();
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

    @Transactional
    public void updateReport(Report report, ReportRequest reportRequest) throws ReportNotFoundException {
        log.info("Updating report with id: " + report.getId());
        report.setDepartment(departmentService.getByName(reportRequest.getDepartmentName()));
        report.setDescription(reportRequest.getDescription());
        report.setStreet(streetService.getByNameAndPartOfTheCity(reportRequest.getStreet(),
                partOfTheCityService.getPartOfTheCityByName(reportRequest.getPartOfTheCity())));

        reportRepository.save(report);
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

    private void saveImage(String imageName, String base64Image) {
        Path path = Paths.get(uploadDir + imageName);
        try {
            Files.write(path, Base64.getDecoder().decode(base64Image));
            log.debug("Image saved to: " + uploadDir + imageName);
        } catch (IOException e) {
            log.info("Unable to save image on path: " + uploadDir + imageName);
        }
    }

    private void deleteImage(String image) {
        try {
            Files.delete(Path.of(uploadDir + image));
            log.info("Deleting file: + " + image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ReportResponse createReportResponseFromReport(Report report) {
        ReportResponse response = reportMapper.reportToReportResponse(report);
        response.setBase64Image(report.encodeImage(uploadDir));
        return response;
    }
}
