package org.unibl.etf.ps.cleanbl.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.repository.PartOfTheCityRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {

    private final PartOfTheCityRepository partOfTheCityRepository;

    public List<PartOfTheCity> getAllPartOfTheCity() {
        return partOfTheCityRepository.findAll();
    }
}
