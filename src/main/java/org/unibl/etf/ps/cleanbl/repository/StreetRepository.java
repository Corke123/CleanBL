package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.model.Street;

import java.util.Optional;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {
    Optional<Street> findByNameAndPartOfTheCity(String name, PartOfTheCity partOfTheCity);
}
