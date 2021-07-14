package org.unibl.etf.ps.cleanbl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.UserStatus;
import org.unibl.etf.ps.cleanbl.repository.UserStatusRepository;
import org.unibl.etf.ps.cleanbl.service.impl.UserStatusServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserStatusServiceImplTest {
    @InjectMocks
    UserStatusServiceImpl userStatusService;

    @Mock
    UserStatusRepository userStatusRepository;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void getActiveStatus_should_return_active_staus() {
        UserStatus userStatus = UserStatus.builder().name("active").id(1L).build();
        when(userStatusRepository.findByName("active")).thenReturn(Optional.of(userStatus));
        assertEquals(userStatus,userStatusService.getActiveStatus());
    }

    @Test
    void getInactiveStatus_should_return_inactive_status() {
        UserStatus userStatus = UserStatus.builder().name("inactive").id(1L).build();
        when(userStatusRepository.findByName("inactive")).thenReturn(Optional.of(userStatus));
        assertEquals(userStatus,userStatusService.getInactiveStatus());
    }

    @Test
    void getActiveStatus_should_throws_exception_when_status_not_exist() {
        when(userStatusRepository.findByName("active")).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> userStatusService.getActiveStatus());
    }

    @Test
    void getInactiveStatus_should_throws_exception_when_status_not_exist() {
        when(userStatusRepository.findByName("inactive")).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> userStatusService.getInactiveStatus());
    }
}
