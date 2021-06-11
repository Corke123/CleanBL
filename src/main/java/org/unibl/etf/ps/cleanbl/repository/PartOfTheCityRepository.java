package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;

import java.util.Optional;

@Repository
public interface PartOfTheCityRepository extends JpaRepository<PartOfTheCity, Long> {
    Optional<PartOfTheCity> findByName(String name);

    Page<PartOfTheCity> findAllByNameContaining(Pageable pageable, String name);
}
