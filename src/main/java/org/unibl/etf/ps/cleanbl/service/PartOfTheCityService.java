package org.unibl.etf.ps.cleanbl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.repository.PartOfTheCityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartOfTheCityService {

    private final PartOfTheCityRepository repository;

    public PartOfTheCity getPartOfTheCityByName(String name) {
        log.info("Getting part of the city by name " + name);
        return repository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException("Unable to find part of the city with name: " + name));
    }

    public List<PartOfTheCity> getAllPartOfTheCity() {
        log.info("Getting parts of the city");
        return repository.findAll();
    }
}
