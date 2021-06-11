package org.unibl.etf.ps.cleanbl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.dto.PartOfTheCityDTO;
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

    public Page<PartOfTheCity> getAllFiltered(PageRequest pageRequest, String name) {
        log.info("Getting parts of the city by: pageSize = " + pageRequest.getPageSize() +
                ", page = " + pageRequest.getPageNumber() +
                ", sort = " + pageRequest.getSort() +
                ", name = " + name);
        if (isEmpty(name)) {
            return repository.findAll(pageRequest);
        } else {
            return repository.findAllByNameContaining(pageRequest, name);
        }
    }

    public PartOfTheCity add(PartOfTheCity partOfTheCity) {
        log.info("Adding new part of the city with name: " + partOfTheCity.getName());
        return repository.save(partOfTheCity);
    }

    public PartOfTheCity update(Long id, PartOfTheCityDTO partOfTheCityDTO) {
        log.info("Updating part of the city with id: " + id);
        PartOfTheCity saved = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("There is no part of the city with id: " + id));
        saved.setName(partOfTheCityDTO.getName());
        return repository.save(saved);
    }

    public void delete(Long id) {
        log.info("Deleting part of the city with id: " + id);
        repository.deleteById(id);
    }

    private boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }

}
