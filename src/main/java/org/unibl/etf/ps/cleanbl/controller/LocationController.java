package org.unibl.etf.ps.cleanbl.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.service.PartOfTheCityService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@AllArgsConstructor
public class LocationController {

    private final PartOfTheCityService partOfTheCityService;

    @GetMapping
    public ResponseEntity<List<PartOfTheCity>> getLocations() {
        return ResponseEntity.ok(partOfTheCityService.getAllPartOfTheCity());
    }
}
