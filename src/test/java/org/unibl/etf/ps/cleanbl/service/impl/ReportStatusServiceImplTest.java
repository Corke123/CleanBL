package org.unibl.etf.ps.cleanbl.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unibl.etf.ps.cleanbl.model.ReportStatus;
import org.unibl.etf.ps.cleanbl.repository.ReportStatusRepository;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

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
                ReportStatus.builder().id(1L).name("poslan").build(),
                ReportStatus.builder().id(2L).name("zavrsen").build());
        when(reportStatusRepository.findAll()).thenReturn(list);
        assertEquals(list, reportStatusService.getAllStatuses());
    }
}
