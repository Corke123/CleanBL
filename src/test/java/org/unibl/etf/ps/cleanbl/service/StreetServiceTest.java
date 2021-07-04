package org.unibl.etf.ps.cleanbl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.model.Street;
import org.unibl.etf.ps.cleanbl.repository.StreetRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StreetServiceTest {

    private static final int PAGE_SIZE = 5;
    private static final int PAGE_NUMBER = 0;
    private static final String DESC = "desc";
    private static final String SORT_BY_CREATION_DATE = "creationDate";
    private static final PageRequest PAGE_REQUEST =
            PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(Sort.Direction.DESC, SORT_BY_CREATION_DATE));
    private static final PartOfTheCity partOfTheCity = PartOfTheCity.builder().id(1L).name("Test pc").build();

    @InjectMocks
    StreetService streetService;

    @Mock
    StreetRepository streetRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_should_return_street_with_given_id() {
        final Long id = 1L;
        Street street = Street.builder().id(id).name("Test street").partOfTheCity(partOfTheCity).build();

        when(streetRepository.findById(id)).thenReturn(Optional.of(street));

        assertEquals(Optional.of(street), streetService.getById(id));
    }

    @Test
    void getByNameAndPartOfTheCity_should_return_street_with_given_name_and_part_of_the_city() {
        String name = "Test street";
        Street street = Street.builder().id(1L).name(name).partOfTheCity(partOfTheCity).build();

        when(streetRepository.findByNameAndPartOfTheCity(name, partOfTheCity)).thenReturn(Optional.of(street));

        assertEquals(street, streetService.getByNameAndPartOfTheCity(name, partOfTheCity));
    }

    @Test
    void getByNameAndPartOfTheCity_should_throws_exception_when_street_not_exist() {
        String name = "Test street";

        assertThrows(RecordNotFoundException.class, () -> streetService.getByNameAndPartOfTheCity(name, partOfTheCity));
    }

    @Test
    void getAllFiltered_should_return_page_of_streets_with_given_page_properties() {
        Page<Street> streets = new PageImpl<>(Arrays.asList(
                Street.builder().id(1L).name("Test street 1").build(),
                Street.builder().id(2L).name("Test street 2").build()
        ));

        when(streetRepository.findAll(PAGE_REQUEST)).thenReturn(streets);

        assertEquals(streets, streetService.getAllFiltered(PAGE_REQUEST, "", ""));
    }

    @Test
    void getAllFiltered_should_return_page_of_streets_with_given_page_properties_and_part_of_the_city() {
        Page<Street> streets = new PageImpl<>(Arrays.asList(
                Street.builder().id(1L).name("Test street 1").partOfTheCity(partOfTheCity).build(),
                Street.builder().id(2L).name("Test street 2").partOfTheCity(partOfTheCity).build()
        ));

        when(streetRepository.findAllByPartOfTheCityName(PAGE_REQUEST, partOfTheCity.getName())).thenReturn(streets);

        assertEquals(streets, streetService.getAllFiltered(PAGE_REQUEST, "", partOfTheCity.getName()));
    }

    @Test
    void getAllFiltered_should_return_page_of_streets_with_given_page_properties_and_name() {
        String street = "Test street";
        Page<Street> streets = new PageImpl<>(Arrays.asList(
                Street.builder().id(1L).name(street).build(),
                Street.builder().id(2L).name(street).build()
        ));

        when(streetRepository.findAllByNameContaining(PAGE_REQUEST, street)).thenReturn(streets);

        assertEquals(streets, streetService.getAllFiltered(PAGE_REQUEST, street, ""));
    }

    @Test
    void getAllFiltered_should_return_page_of_streets_with_given_page_properties_and_part_of_the_city_and_name() {
        String street = "Test street";
        Page<Street> streets = new PageImpl<>(Arrays.asList(
                Street.builder().id(1L).name(street).partOfTheCity(partOfTheCity).build(),
                Street.builder().id(2L).name(street).partOfTheCity(partOfTheCity).build()
        ));

        when(streetRepository.findAllByNameContainingAndPartOfTheCityName(PAGE_REQUEST, street, partOfTheCity.getName()))
                .thenReturn(streets);

        assertEquals(streets, streetService.getAllFiltered(PAGE_REQUEST, street, partOfTheCity.getName()));
    }

    @Test
    void add_should_return_saved_street() {
        Street street = Street.builder().id(1L).name("Test street").partOfTheCity(partOfTheCity).build();

        when(streetRepository.save(street)).thenReturn(street);

        assertEquals(street, streetService.save(street));
    }

    @Test
    void delete_should_call_delete_from_street_repository() {
        streetService.delete(1L);
        verify(streetRepository).deleteById(1L);
    }
}
