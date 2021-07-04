package org.unibl.etf.ps.cleanbl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.dto.StreetRequest;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.model.Street;
import org.unibl.etf.ps.cleanbl.repository.StreetRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreetService {
    private final StreetRepository streetRepository;
    private final PartOfTheCityService partOfTheCityService;

    public Optional<Street> getById(Long id) {
        log.info("Find street by id: " + id);
        return streetRepository.findById(id);
    }

    public Street getByNameAndPartOfTheCity(String name, PartOfTheCity partOfTheCity) {
        log.info("Find street by name: " + name + " and part of the city: " + partOfTheCity.getName());
        return streetRepository.findByNameAndPartOfTheCity(name, partOfTheCity)
                .orElseThrow(() -> new RecordNotFoundException("Unable to find street with name: " + name));
    }

    public Page<Street> getAllFiltered(PageRequest pageRequest, String name, String partOfTheCity) {
        log.info("Getting streets by: pageSize = " + pageRequest.getPageSize() +
                ", page = " + pageRequest.getPageNumber() +
                ", sort = " + pageRequest.getSort() +
                ", name = " + name +
                ", part of the city = " + partOfTheCity);
        if (isEmpty(name) && isEmpty(partOfTheCity)) {
            return streetRepository.findAll(pageRequest);
        } else if (isEmpty(name)) {
            return streetRepository.findAllByPartOfTheCityName(pageRequest, partOfTheCity);
        } else if (isEmpty(partOfTheCity)) {
            return streetRepository.findAllByNameContaining(pageRequest, name);
        } else {
            return streetRepository.findAllByNameContainingAndPartOfTheCityName(pageRequest, name, partOfTheCity);
        }
    }

    public Street save(Street street) {
        log.info("Adding new street with name: " + street.getName());
        return streetRepository.save(street);
    }

    public void delete(Long id) {
        log.info("Deleting street with id: " + id);
        streetRepository.deleteById(id);
    }

    private boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }
}
