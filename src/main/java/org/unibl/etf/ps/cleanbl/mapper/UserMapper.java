package org.unibl.etf.ps.cleanbl.mapper;

import org.mapstruct.Mapper;
import org.unibl.etf.ps.cleanbl.dto.RegisterRequest;
import org.unibl.etf.ps.cleanbl.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterRequest registerRequest);
}
