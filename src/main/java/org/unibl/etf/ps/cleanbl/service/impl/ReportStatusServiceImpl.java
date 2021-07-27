package org.unibl.etf.ps.cleanbl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.ReportStatus;
import org.unibl.etf.ps.cleanbl.repository.ReportStatusRepository;
import org.unibl.etf.ps.cleanbl.service.ReportStatusService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportStatusServiceImpl implements ReportStatusService {
    private final ReportStatusRepository repository;

    @Override
    public ReportStatus getSentStatus() {
        log.info("Find status with name poslan");
        return repository.findByName("poslan")
                .orElseThrow(() -> new RecordNotFoundException("Unable to find report status with name 'poslan'"));
    }

    @Override
    public ReportStatus getInProcessStatus() {
        log.info("Find status with name u procesu");
        return repository.findByName("u procesu")
                .orElseThrow(() -> new RecordNotFoundException("Unable to find report status with name 'u procesu'"));
    }

    @Override
    public ReportStatus getCompletedStatus() {
        log.info("Find status with name zavrsen");
        return repository.findByName("zavrsen")
                .orElseThrow(() -> new RecordNotFoundException("Unable to find report status with name 'zavrsen'"));
    }

    @Override
    public List<ReportStatus> getAllStatuses() {
        log.info("Find all statuses");
        return repository.findAll();
    }
}
