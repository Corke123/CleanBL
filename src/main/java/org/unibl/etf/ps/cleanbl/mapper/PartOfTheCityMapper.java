package org.unibl.etf.ps.cleanbl.mapper;

import org.mapstruct.Mapper;
import org.unibl.etf.ps.cleanbl.dto.PartOfTheCityDTO;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;

@Mapper(componentModel = "spring")
public interface PartOfTheCityMapper {
    PartOfTheCityDTO toDTO(PartOfTheCity partOfTheCity);
}
