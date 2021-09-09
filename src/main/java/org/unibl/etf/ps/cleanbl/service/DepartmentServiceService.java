package org.unibl.etf.ps.cleanbl.service;

import org.unibl.etf.ps.cleanbl.model.DepartmentService;

import java.util.List;

public interface DepartmentServiceService {

    List<DepartmentService> getDepartmentServicesForUsersDepartment();

    List<DepartmentService> getAll();

    DepartmentService getByName(String name);
}
