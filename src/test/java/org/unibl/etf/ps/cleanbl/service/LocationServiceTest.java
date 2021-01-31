package org.unibl.etf.ps.cleanbl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.repository.PartOfTheCityRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private PartOfTheCityRepository partOfTheCityRepository;

    private LocationService underTest;

    @BeforeEach
    public void init() {
        underTest = new LocationService(partOfTheCityRepository);
    }

    @Test
    public void itShouldReturnPartsOfTheCity() {
        // Given
        List<PartOfTheCity> partOfTheCityList = Arrays.asList(
                new PartOfTheCity(1L, "City Block1", null),
                new PartOfTheCity(1L, "City Block2", null),
                new PartOfTheCity(1L, "City Block3", null)
        );

        given(partOfTheCityRepository.findAll()).willReturn(partOfTheCityList);

        // When
        List<PartOfTheCity> resultList = underTest.getAllPartOfTheCity();

        // Than
        assertThat(resultList.size() == 3);
    }
}