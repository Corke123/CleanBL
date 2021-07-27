package org.unibl.etf.ps.cleanbl.service;

import org.unibl.etf.ps.cleanbl.model.ReportStatus;

import java.util.List;

public interface ReportStatusService {

    ReportStatus getSentStatus();
    ReportStatus getInProcessStatus();
    ReportStatus getCompletedStatus();
    List<ReportStatus> getAllStatuses();
}
