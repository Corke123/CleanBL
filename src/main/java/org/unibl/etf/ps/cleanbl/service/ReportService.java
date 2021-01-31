package org.unibl.etf.ps.cleanbl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.ps.cleanbl.dto.CommentDTO;
import org.unibl.etf.ps.cleanbl.dto.CommentRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportResponse;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.exception.ReportNotFoundException;
import org.unibl.etf.ps.cleanbl.model.*;
import org.unibl.etf.ps.cleanbl.repository.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final String IMAGE_EXTENSION = ".jpg";

    private final ReportRepository reportRepository;
    private final ReportStatusRepository reportStatusRepository;
    private final DepartmentRepository departmentRepository;
    private final PartOfTheCityRepository partOfTheCityRepository;
    private final StreetRepository streetRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, ReportStatusRepository reportStatusRepository,
                         DepartmentRepository departmentRepository, PartOfTheCityRepository partOfTheCityRepository,
                         StreetRepository streetRepository, UserRepository userRepository,
                         CommentRepository commentRepository) {
        this.reportRepository = reportRepository;
        this.reportStatusRepository = reportStatusRepository;
        this.departmentRepository = departmentRepository;
        this.partOfTheCityRepository = partOfTheCityRepository;
        this.streetRepository = streetRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }


    @Transactional
    public ReportResponse saveReport(ReportRequest reportRequest) {
        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        EndUser user = (EndUser) userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("There is no user with username: " + username));
        Department department = departmentRepository.findByName(reportRequest.getDepartmentName())
                .orElseThrow(() -> new RecordNotFoundException("Unable to find department with name: " + reportRequest.getDepartmentName()));
        PartOfTheCity partOfTheCity = partOfTheCityRepository.findByName(reportRequest.getPartOfTheCity())
                .orElseThrow(() -> new RecordNotFoundException("Unable to find part of the city with name: " + reportRequest.getPartOfTheCity()));
        Street street = streetRepository.findByNameAndPartOfTheCity(reportRequest.getStreet(), partOfTheCity)
                .orElseThrow(() -> new RecordNotFoundException("Unable to find street with name: " + reportRequest.getStreet()));
        ReportStatus reportStatus = reportStatusRepository.findByName("received")
                .orElseThrow(() -> new RecordNotFoundException("Unable to find report status with name received"));

        String imageName = UUID.randomUUID().toString() + IMAGE_EXTENSION;

        Path path = Paths.get(uploadDir + imageName);
        try {
            Files.write(path, Base64.getDecoder().decode(reportRequest.getBase64Image()));
            log.debug("Image saved to: " + uploadDir + imageName);
        } catch (IOException e) {
            log.info("Unable to save image on path: " + uploadDir + imageName);
        }

        Report report = new Report(user, reportRequest.getDescription(), imageName,
                reportStatus,  street, department);

        Report saved = reportRepository.save(report);

        return new ReportResponse(saved.getId(),
                saved.getEndUser().getUsername(),
                saved.getDescription(),
                saved.getCreatedAt(),
                saved.getReportStatus().getName(),
                saved.getStreet().getName() + " - " + saved.getStreet().getPartOfTheCity().getName(),
                saved.getDepartment().getName(),
                reportRequest.getBase64Image());
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAll().stream()
                .map(r ->
                    new ReportResponse(
                        r.getId(),
                        r.getEndUser().getUsername(),
                        r.getDescription(),
                        r.getCreatedAt(),
                        r.getReportStatus().getName(),
                        r.getStreet().getName() + " - " + r.getStreet().getPartOfTheCity().getName(),
                        r.getDepartment().getName(),
                        r.encodeImage(uploadDir))
                    )
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReportResponse getReport(Long id) throws ReportNotFoundException {
        Report report = reportRepository.findById(id).orElseThrow(() -> new ReportNotFoundException("There is no report with id: " + id));

        return new ReportResponse(report.getId(), report.getEndUser().getUsername(), report.getDescription(),
                report.getCreatedAt(), report.getReportStatus().getName(),
                report.getStreet().getName() + " - " + report.getStreet().getPartOfTheCity().getName(),
                report.getDepartment().getName(),
                report.encodeImage(uploadDir));
    }

    @Transactional
    public boolean deleteReport(Long id) throws ReportNotFoundException {
        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        boolean isDepartmentOfficer = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_DepartmentOfficer"));
        Report report = reportRepository.findById(id).orElseThrow(() -> new ReportNotFoundException("There is no report with id: " + id));
        if (isDepartmentOfficer || report.getEndUser().getUsername().equals(username)) {
            reportRepository.delete(report);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean updateReport(Long id, ReportRequest reportRequest) throws ReportNotFoundException {
        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        boolean isDepartmentOfficer = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_DepartmentOfficer"));
        Report report = reportRepository.findById(id).orElseThrow(() -> new ReportNotFoundException("There is no report with id: " + id));
        if (isDepartmentOfficer || report.getEndUser().getUsername().equals(username)) {
            Department department = departmentRepository.findByName(reportRequest.getDepartmentName())
                    .orElseThrow(() -> new RecordNotFoundException("Unable to find department with name: "
                    + reportRequest.getDepartmentName()));
            PartOfTheCity partOfTheCity = partOfTheCityRepository.findByName(reportRequest.getPartOfTheCity())
                    .orElseThrow(() -> new RecordNotFoundException("Unable to find part of the city with name: " + reportRequest.getPartOfTheCity()));
            Street street = streetRepository.findByNameAndPartOfTheCity(reportRequest.getStreet(), partOfTheCity)
                    .orElseThrow(() -> new RecordNotFoundException("Unable to find street with name " + reportRequest.getStreet() + " in the " + partOfTheCity.getName()));

            String imageName = UUID.randomUUID().toString() + IMAGE_EXTENSION;
            Path path = Paths.get(uploadDir + imageName);
            try {
                Files.write(path, Base64.getDecoder().decode(reportRequest.getBase64Image()));
                log.debug("Image saved to: " + uploadDir + imageName);
            } catch (IOException e) {
                log.info("Unable to save image on path: " + uploadDir + imageName);
            }

            report.setDepartment(department);
            report.setDescription(reportRequest.getDescription());
            report.setStreet(street);
            report.setImagePath(imageName);

            reportRepository.save(report);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsForReport(Long reportId) throws ReportNotFoundException {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new ReportNotFoundException("There is no report with id: " + reportId));
        return commentRepository.findByReport(report).stream()
                .map(c -> new CommentDTO(c.getReport().getId(), c.getDateOfPublication(), c.getContent(), c.getEndUser().getUsername()))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDTO addComment(Long reportId, CommentRequest commentRequest) throws ReportNotFoundException {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new ReportNotFoundException("There is no report with id: " + reportId));
        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        EndUser endUser = (EndUser) userRepository.findByUsername(username).orElseThrow(() -> new RecordNotFoundException("There is no user with username: " + username));
        Comment comment = new Comment(report, commentRequest.getContent(), endUser);
        commentRepository.save(comment);
        return new CommentDTO(report.getId(), comment.getDateOfPublication(), comment.getContent(), comment.getEndUser().getUsername());
    }
}
