package org.unibl.etf.ps.cleanbl.service;

import org.unibl.etf.ps.cleanbl.model.UserStatus;

public interface UserStatusService {

    UserStatus getActiveStatus();
    UserStatus getInactiveStatus();
}
