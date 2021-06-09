package org.unibl.etf.ps.cleanbl.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.ps.cleanbl.dto.PartOfTheCityDTO;
import org.unibl.etf.ps.cleanbl.mapper.PartOfTheCityMapper;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.service.PartOfTheCityService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/parts-of-the-city")
@AllArgsConstructor
public class PartOfTheCityController {

    private final PartOfTheCityService partOfTheCityService;
    private final PartOfTheCityMapper partOfTheCityMapper;

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
}
