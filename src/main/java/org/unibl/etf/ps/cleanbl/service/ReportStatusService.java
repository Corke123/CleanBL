package org.unibl.etf.ps.cleanbl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.ReportStatus;
import org.unibl.etf.ps.cleanbl.repository.ReportStatusRepository;

@Service
@RequiredArgsConstructor
public class ReportStatusService {
    private final ReportStatusRepository repository;

    public ReportStatus getSentStatus() {
        return repository.findByName("poslan")
                .orElseThrow(() -> new RecordNotFoundException("Unable to find report status with name 'poslan'"));
    }

    public ReportStatus getInProcessStatus() {
        return repository.findByName("u procesu")
                .orElseThrow(() -> new RecordNotFoundException("Unable to find report status with name 'u procesu'"));
    }

    public ReportStatus getCompletedStatus() {
        return repository.findByName("zavrsen")
                .orElseThrow(() -> new RecordNotFoundException("Unable to find report status with name 'zavrsen'"));
    }
}
