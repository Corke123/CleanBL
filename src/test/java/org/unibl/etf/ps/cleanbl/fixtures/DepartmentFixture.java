package org.unibl.etf.ps.cleanbl.fixtures;

import org.unibl.etf.ps.cleanbl.model.Department;

public class DepartmentFixture {
    public static Department.DepartmentBuilder createDepartment() {
        return Department.builder().id(1L).name("Department 1");
    }
}

