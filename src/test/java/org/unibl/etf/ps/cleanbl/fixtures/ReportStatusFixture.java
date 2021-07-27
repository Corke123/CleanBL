package org.unibl.etf.ps.cleanbl.fixtures;

import org.unibl.etf.ps.cleanbl.model.ReportStatus;

public class ReportStatusFixture {
    public static ReportStatus.ReportStatusBuilder createSent() {
        return ReportStatus.builder().id(1L).name("poslan");
    }

    public static ReportStatus.ReportStatusBuilder createInProcess() {
        return ReportStatus.builder().id(2L).name("u procesu");
    }

    public static ReportStatus.ReportStatusBuilder createCompleted() {
        return ReportStatus.builder().id(3L).name("zavrsen");
    }
}
