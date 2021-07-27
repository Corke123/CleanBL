package org.unibl.etf.ps.cleanbl.fixtures;

import org.unibl.etf.ps.cleanbl.model.ReportStatus;

public class ReportStatusFixture {
    public static ReportStatus createSent() {
        return ReportStatus.builder().id(1L).name("poslan").build();
    }

    public static ReportStatus createInProcess() {
        return ReportStatus.builder().id(2L).name("u procesu").build();
    }

    public static ReportStatus createCompleted() {
        return ReportStatus.builder().id(3L).name("završen").build();
    }
}
