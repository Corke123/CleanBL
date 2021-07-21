package org.unibl.etf.ps.cleanbl.fixtures;

import org.unibl.etf.ps.cleanbl.model.UserStatus;

public class UserStatusFixture {
    public static UserStatus.UserStatusBuilder createUserStatus() {
        return UserStatus.builder().id(1L).name("active");
    }
}

