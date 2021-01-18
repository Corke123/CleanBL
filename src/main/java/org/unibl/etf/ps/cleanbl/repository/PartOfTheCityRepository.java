package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;

import java.util.Optional;

public interface PartOfTheCityRepository extends JpaRepository<PartOfTheCity, Long> {
    Optional<PartOfTheCity> findByName(String name);
}
