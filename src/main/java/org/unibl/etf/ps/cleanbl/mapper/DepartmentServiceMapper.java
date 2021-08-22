package org.unibl.etf.ps.cleanbl.mapper;

import org.mapstruct.Mapper;
import org.unibl.etf.ps.cleanbl.dto.DepartmentServiceDTO;
import org.unibl.etf.ps.cleanbl.model.DepartmentService;

@Mapper(componentModel = "spring")
public interface DepartmentServiceMapper {
    DepartmentServiceDTO toDTO(DepartmentService departmentService);
}
