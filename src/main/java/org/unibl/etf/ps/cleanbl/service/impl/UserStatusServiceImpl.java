package org.unibl.etf.ps.cleanbl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.UserStatus;
import org.unibl.etf.ps.cleanbl.repository.UserStatusRepository;
import org.unibl.etf.ps.cleanbl.service.UserStatusService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusServiceImpl implements UserStatusService {

    private static final String ACTIVE_STATUS = "active";
    private static final String INACTIVE_STATUS = "inactive";

    private final UserStatusRepository repository;

    @Override
    public UserStatus getActiveStatus() {
        return repository.findByName(ACTIVE_STATUS)
                .orElseThrow(() -> new RecordNotFoundException("There is no status " + ACTIVE_STATUS));
    }

    @Override
    public UserStatus getInactiveStatus() {
        return repository.findByName(INACTIVE_STATUS)
                .orElseThrow(() -> new RecordNotFoundException("There is no status " + INACTIVE_STATUS));
    }
}
