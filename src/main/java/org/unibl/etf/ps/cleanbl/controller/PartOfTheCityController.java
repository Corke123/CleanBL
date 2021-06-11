package org.unibl.etf.ps.cleanbl.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.PartOfTheCityDTO;
import org.unibl.etf.ps.cleanbl.mapper.PartOfTheCityMapper;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.service.PartOfTheCityService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/parts-of-the-city")
@AllArgsConstructor
public class PartOfTheCityController {

    private final PartOfTheCityService partOfTheCityService;
    private final PartOfTheCityMapper partOfTheCityMapper;

    private static final String PAGE = "0";
    private static final String SIZE = "10";

    @GetMapping
    public ResponseEntity<List<PartOfTheCity>> getPartsOfTheCity() {
        return ResponseEntity.ok(partOfTheCityService.getAllPartOfTheCity());
    }

    @GetMapping("/simple")
    public ResponseEntity<List<PartOfTheCityDTO>> getPartsOfTheCitySimple() {
        return ResponseEntity.ok(partOfTheCityService.getAllPartOfTheCity()
                .stream()
                .map(partOfTheCityMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/pageable")
    @PreAuthorize("hasAnyAuthority('PartOfTheCity_Read', 'Role_Admin')")
    public ResponseEntity<Page<PartOfTheCityDTO>> getFiltered(
            @RequestParam(value = "page", defaultValue = PAGE) Integer page,
            @RequestParam(value = "size", defaultValue = SIZE) Integer size,
            @RequestParam(value = "sort", defaultValue = "name") String sortColumn,
            @RequestParam(value = "order", defaultValue = "ASC") String sortOrder,
            @RequestParam(value = "name", defaultValue = "") String name) {
        return ResponseEntity.ok(partOfTheCityService.getAllFiltered(
                PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortColumn)),
                name).map(partOfTheCityMapper::toDTO));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('PartOfTheCity_Create', 'Role_Admin')")
    public ResponseEntity<PartOfTheCityDTO> add(@Valid @RequestBody PartOfTheCityDTO partOfTheCityDTO) {
        PartOfTheCity created = partOfTheCityService.add(partOfTheCityMapper.toEntity(partOfTheCityDTO));
        return ResponseEntity.created(URI.create("/api/v1/parts-of-the-city/" + created.getId()))
                .body(partOfTheCityMapper.toDTO(created));
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('PartOfTheCity_Update', 'Role_Admin')")
    public ResponseEntity<PartOfTheCityDTO> update(@PathVariable("id") Long id,
                                                   @Valid @RequestBody PartOfTheCityDTO partOfTheCityDTO) {
        return ResponseEntity.ok(partOfTheCityMapper.toDTO(partOfTheCityService.update(id, partOfTheCityDTO)));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('PartOfTheCity_Delete', 'Role_Admin')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        partOfTheCityService.delete(id);
        return ResponseEntity.ok().build();
    }
}
