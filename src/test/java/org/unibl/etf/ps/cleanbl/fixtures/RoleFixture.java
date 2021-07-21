package org.unibl.etf.ps.cleanbl.fixtures;

import org.unibl.etf.ps.cleanbl.model.Role;

public class RoleFixture {
    public static Role.RoleBuilder createRole() {
        return Role.builder().name("DepartmentOfficer");
    }
}


