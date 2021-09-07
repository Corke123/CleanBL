package org.unibl.etf.ps.cleanbl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.unibl.etf.ps.cleanbl.dto.LocationsDTO;
import org.unibl.etf.ps.cleanbl.model.Report;

@Mapper(componentModel = "spring")
public interface LocationsMapper {
    @Mapping(target = "status", source = "report.reportStatus.name")
    LocationsDTO toDTO(Report report);
}
