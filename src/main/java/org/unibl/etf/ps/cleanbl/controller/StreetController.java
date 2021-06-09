package org.unibl.etf.ps.cleanbl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.StreetDTO;
import org.unibl.etf.ps.cleanbl.dto.StreetRequest;
import org.unibl.etf.ps.cleanbl.mapper.StreetMapper;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.model.Street;
import org.unibl.etf.ps.cleanbl.service.PartOfTheCityService;
import org.unibl.etf.ps.cleanbl.service.StreetService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "/api/v1/streets")
@RequiredArgsConstructor
public class StreetController {

    private final StreetService streetService;
    private final StreetMapper streetMapper;
    private final PartOfTheCityService partOfTheCityService;

    private static final String PAGE = "0";
    private static final String SIZE = "10";

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Street_Read', 'Role_Admin')")
    public ResponseEntity<Page<StreetDTO>> getFiltered(@RequestParam(value = "page", defaultValue = PAGE) Integer page,
                                                       @RequestParam(value = "size", defaultValue = SIZE) Integer size,
                                                       @RequestParam(value = "sort", defaultValue = "name") String sortColumn,
                                                       @RequestParam(value = "order", defaultValue = "ASC") String sortOrder,
                                                       @RequestParam(value = "name", defaultValue = "") String name,
                                                       @RequestParam(value = "part-of-the-city", defaultValue = "") String partOfTheCity) {
        return ResponseEntity.ok(
                streetService.getAllFiltered(
                        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortColumn)),
                        name,
                        partOfTheCity
                ).map(streetMapper::toDTO));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Street_Create', 'Role_Admin')")
    public ResponseEntity<StreetDTO> add(@Valid @RequestBody StreetRequest streetRequest) {
        PartOfTheCity partOfTheCity = partOfTheCityService.getPartOfTheCityByName(streetRequest.getPartOfTheCity());
        Street created = streetService.add(streetMapper.toEntity(streetRequest, partOfTheCity));
        return ResponseEntity.created(URI.create("/api/v1/streets/" + created.getId()))
                .body(streetMapper.toDTO(created));
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('Street_Update', 'Role_Admin')")
    public ResponseEntity<StreetDTO> update(@PathVariable("id") Long id, @Valid @RequestBody StreetRequest streetRequest) {
        return ResponseEntity.ok(streetMapper.toDTO(streetService.update(id, streetRequest)));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('Street_Delete', 'Role_Admin')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        streetService.delete(id);
        return ResponseEntity.ok().build();
    }
}
