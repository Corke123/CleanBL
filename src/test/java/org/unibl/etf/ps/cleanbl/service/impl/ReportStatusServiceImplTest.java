package org.unibl.etf.ps.cleanbl.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unibl.etf.ps.cleanbl.fixtures.ReportStatusFixture;
import org.unibl.etf.ps.cleanbl.model.ReportStatus;
import org.unibl.etf.ps.cleanbl.repository.ReportStatusRepository;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReportStatusServiceImplTest {
    @InjectMocks
    ReportStatusServiceImpl reportStatusService;

    @Mock
    ReportStatusRepository reportStatusRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStatuses_should_return_all_statuses() {
        List<ReportStatus> list = Arrays.asList(
                ReportStatusFixture.createSent().build(),
                ReportStatusFixture.createCompleted().build());
        when(reportStatusRepository.findAll()).thenReturn(list);
        assertEquals(list, reportStatusService.getAllStatuses());
    }

    @Test
    void getSentStatus_should_return_sent_status() {
        ReportStatus reportStatus = ReportStatusFixture.createSent().build();
        when(reportStatusRepository.findByName("poslan")).thenReturn(Optional.of(reportStatus));
        assertEquals(reportStatus, reportStatusService.getSentStatus());
    }

    @Test
    void getInProcessStatus_should_return_in_process_status() {
        ReportStatus reportStatus = ReportStatusFixture.createInProcess().build();
        when(reportStatusRepository.findByName("u procesu")).thenReturn(Optional.of(reportStatus));
        assertEquals(reportStatus, reportStatusService.getInProcessStatus());
    }

    @Test
    void getCompletedStatus_should_return_completed_status() {
        ReportStatus reportStatus = ReportStatusFixture.createCompleted().build();
        when(reportStatusRepository.findByName("zavrsen")).thenReturn(Optional.of(reportStatus));
        assertEquals(reportStatus, reportStatusService.getCompletedStatus());
    }

}
