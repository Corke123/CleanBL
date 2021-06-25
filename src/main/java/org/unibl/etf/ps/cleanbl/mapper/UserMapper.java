package org.unibl.etf.ps.cleanbl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unibl.etf.ps.cleanbl.dto.DepartmentOfficerDTO;
import org.unibl.etf.ps.cleanbl.dto.RegisterRequest;
import org.unibl.etf.ps.cleanbl.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntityFromRegisterRequest(RegisterRequest registerRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    User toUserFromDepartmentOfficerDTO(DepartmentOfficerDTO departmentOfficerDTO);

    @Mapping(target = "department", source = "department.name")
    DepartmentOfficerDTO toDepartmentOfficer(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    User updateUserFromDepartmentOfficerDTO(DepartmentOfficerDTO departmentOfficerDTO, @MappingTarget User user);
}
