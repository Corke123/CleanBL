package org.unibl.etf.ps.cleanbl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.ReportStatus;
import org.unibl.etf.ps.cleanbl.repository.ReportStatusRepository;
import org.unibl.etf.ps.cleanbl.service.ReportStatusService;

@Service
@RequiredArgsConstructor
public class ReportStatusServiceImpl implements ReportStatusService {
    private final ReportStatusRepository repository;

    @Override
    public ReportStatus getSentStatus() {
        return repository.findByName("poslan")
                .orElseThrow(() -> new RecordNotFoundException("Unable to find report status with name 'poslan'"));
    }

    @Override
    public ReportStatus getInProcessStatus() {
        return repository.findByName("u procesu")
                .orElseThrow(() -> new RecordNotFoundException("Unable to find report status with name 'u procesu'"));
    }

    @Override
    public ReportStatus getCompletedStatus() {
        return repository.findByName("zavrsen")
                .orElseThrow(() -> new RecordNotFoundException("Unable to find report status with name 'zavrsen'"));
    }
}
