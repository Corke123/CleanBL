package org.unibl.etf.ps.cleanbl.service;

import org.unibl.etf.ps.cleanbl.model.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> getDepartments();
    Department getByName(String name);
}
