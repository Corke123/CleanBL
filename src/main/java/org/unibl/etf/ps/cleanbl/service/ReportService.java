package org.unibl.etf.ps.cleanbl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.ps.cleanbl.dto.ReportRequest;
import org.unibl.etf.ps.cleanbl.dto.ReportResponse;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
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

    @Autowired
    public ReportService(ReportRepository reportRepository, ReportStatusRepository reportStatusRepository,
                         DepartmentRepository departmentRepository, PartOfTheCityRepository partOfTheCityRepository,
                         StreetRepository streetRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.reportStatusRepository = reportStatusRepository;
        this.departmentRepository = departmentRepository;
        this.partOfTheCityRepository = partOfTheCityRepository;
        this.streetRepository = streetRepository;
        this.userRepository = userRepository;
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

        return new ReportResponse(saved.getId().toString(),
                saved.getEndUser().getUsername(),
                saved.getDescription(),
                saved.getCreatedAt(),
                saved.getReportStatus().getName(),
                saved.getStreet().getName() + " - " + saved.getStreet().getPartOfTheCity().getName(),
                saved.getDepartment().getName());
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAll().stream()
                .map(r -> new ReportResponse(
                        r.getId().toString(),
                        r.getEndUser().getUsername(),
                        r.getDescription(),
                        r.getCreatedAt(),
                        r.getReportStatus().getName(),
                        r.getStreet().getName() + " - " + r.getStreet().getPartOfTheCity().getName(),
                        r.getDepartment().getName()))
                .collect(Collectors.toList());
    }
}
