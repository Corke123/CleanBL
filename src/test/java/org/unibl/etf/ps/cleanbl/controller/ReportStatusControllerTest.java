package org.unibl.etf.ps.cleanbl.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.unibl.etf.ps.cleanbl.model.ReportStatus;
import org.unibl.etf.ps.cleanbl.service.ReportStatusService;

import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportStatusControllerTest {

    @InjectMocks
    ReportStatusController reportStatusController;

    @Mock
    ReportStatusService reportStatusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReportServices_should_return_list_of_report_statuses() {
        List<ReportStatus> list = Arrays.asList(
                ReportStatus.builder().id(1L).name("poslan").build(),
                ReportStatus.builder().id(2L).name("završen").build());
        when(reportStatusService.getAllStatuses()).thenReturn(list);

        ResponseEntity<List<ReportStatus>> responseEntity = reportStatusController.getReportServices();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(list, responseEntity.getBody());
    }
}
