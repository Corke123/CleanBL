package org.unibl.etf.ps.cleanbl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.unibl.etf.ps.cleanbl.dto.RegisterRequest;
import org.unibl.etf.ps.cleanbl.model.EndUser;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "numberOfPositivePoints", constant = "0")
    @Mapping(target = "numberOfNegativePoints", constant = "0")
    EndUser toEntity(RegisterRequest registerRequest);
}
