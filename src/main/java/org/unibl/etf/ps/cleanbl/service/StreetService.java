package org.unibl.etf.ps.cleanbl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.model.Street;
import org.unibl.etf.ps.cleanbl.repository.StreetRepository;

@Service
@RequiredArgsConstructor
public class StreetService {
    private final StreetRepository streetRepository;

    public Street getByNameAndPartOfTheCity(String name, PartOfTheCity partOfTheCity) {
        return streetRepository.findByNameAndPartOfTheCity(name, partOfTheCity)
                .orElseThrow(() -> new RecordNotFoundException("Unable to find street with name: " + name));
    }
}
