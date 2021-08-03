package org.unibl.etf.ps.cleanbl.mapper;

import org.mapstruct.Mapper;
import org.unibl.etf.ps.cleanbl.dto.DepartmentDTO;
import org.unibl.etf.ps.cleanbl.model.Department;
@Mapper(componentModel = "spring")
public interface DepartmentMapper {
     DepartmentDTO toDTO(Department department);
}
