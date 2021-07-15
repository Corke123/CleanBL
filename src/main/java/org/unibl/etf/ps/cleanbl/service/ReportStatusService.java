package org.unibl.etf.ps.cleanbl.service;

import org.unibl.etf.ps.cleanbl.model.ReportStatus;

public interface ReportStatusService {

    ReportStatus getSentStatus();
    ReportStatus getInProcessStatus();
    ReportStatus getCompletedStatus();
}
