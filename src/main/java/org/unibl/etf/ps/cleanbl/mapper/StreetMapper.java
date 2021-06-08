package org.unibl.etf.ps.cleanbl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.unibl.etf.ps.cleanbl.dto.StreetDTO;
import org.unibl.etf.ps.cleanbl.dto.StreetRequest;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.model.Street;

@Mapper(componentModel = "spring")
public interface StreetMapper {
    @Mapping(target = "partOfTheCity", source = "partOfTheCity.name")
    StreetDTO toDTO(Street street);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "streetRequest.name")
    @Mapping(target = "partOfTheCity", source = "partOfTheCity")
    Street toEntity(StreetRequest streetRequest, PartOfTheCity partOfTheCity);
}