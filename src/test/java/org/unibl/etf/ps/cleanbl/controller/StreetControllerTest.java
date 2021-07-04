package org.unibl.etf.ps.cleanbl.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.unibl.etf.ps.cleanbl.dto.StreetDTO;
import org.unibl.etf.ps.cleanbl.dto.StreetRequest;
import org.unibl.etf.ps.cleanbl.mapper.StreetMapper;
import org.unibl.etf.ps.cleanbl.model.PartOfTheCity;
import org.unibl.etf.ps.cleanbl.model.Street;
import org.unibl.etf.ps.cleanbl.repository.StreetRepository;
import org.unibl.etf.ps.cleanbl.service.PartOfTheCityService;
import org.unibl.etf.ps.cleanbl.service.StreetService;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StreetControllerTest {

    private static final int PAGE_SIZE = 5;
    private static final int PAGE_NUMBER = 1;
    private static final String DESC = "desc";
    private static final String SORT_BY_CREATION_DATE = "creationDate";
    private static final PageRequest PAGE_REQUEST =
            PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(Sort.Direction.DESC, SORT_BY_CREATION_DATE));
    private static final PartOfTheCity partOfTheCity = PartOfTheCity.builder().id(1L).name("Test pc").build();

    @InjectMocks
    StreetController streetController;

    @Mock
    StreetRepository streetRepository;

    @Mock
    PartOfTheCityService partOfTheCityService;

    @Mock
    StreetService streetService;

    @Mock
    StreetMapper streetMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFiltered_should_return_filtered_page_of_streets() {
        Street street1 = Street.builder().id(1L).name("Test street 1").partOfTheCity(partOfTheCity).build();
        Street street2 = Street.builder().id(2L).name("Test street 2").partOfTheCity(partOfTheCity).build();

        StreetDTO streetDTO1 =
                StreetDTO.builder().id(1L).name("Test street 1").partOfTheCity(partOfTheCity.getName()).build();
        StreetDTO streetDTO2 =
                StreetDTO.builder().id(2L).name("Test street 2").partOfTheCity(partOfTheCity.getName()).build();

        Page<Street> streets = new PageImpl<>(Arrays.asList(street1, street2));
        Page<StreetDTO> streetsDTO = new PageImpl<>(Arrays.asList(streetDTO1, streetDTO2));

        when(streetService.getAllFiltered(PAGE_REQUEST, "", "")).thenReturn(streets);
        when(streetMapper.toDTO(street1)).thenReturn(streetDTO1);
        when(streetMapper.toDTO(street2)).thenReturn(streetDTO2);

        ResponseEntity<Page<StreetDTO>> responseEntity =
                streetController.getFiltered(PAGE_NUMBER, PAGE_SIZE, SORT_BY_CREATION_DATE, DESC, "", "");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(streetsDTO, responseEntity.getBody());
    }

    @Test
    void add_should_return_street() {
        final Long id = 1L;
        StreetRequest streetRequest =
                StreetRequest.builder().name("Test street 1").partOfTheCity(partOfTheCity.getName()).build();
        Street street = Street.builder().id(id).name(streetRequest.getName()).partOfTheCity(partOfTheCity).build();

        StreetDTO streetDTO =
                StreetDTO.builder().id(id).name(streetRequest.getName()).partOfTheCity(partOfTheCity.getName()).build();

        when(partOfTheCityService.getPartOfTheCityByName(streetRequest.getPartOfTheCity())).thenReturn(partOfTheCity);
        when(streetMapper.toEntity(streetRequest, partOfTheCity)).thenReturn(street);
        when(streetService.save(street)).thenReturn(street);
        when(streetMapper.toDTO(street)).thenReturn(streetDTO);

        ResponseEntity<StreetDTO> responseEntity = streetController.add(streetRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(streetDTO, responseEntity.getBody());
    }

    @Test
    void update_should_return_updated_street() {
        final Long id = 1L;
        StreetRequest streetRequest =
                StreetRequest.builder().name("Test street 1").partOfTheCity(partOfTheCity.getName()).build();
        Street street = Street.builder().id(id).name(streetRequest.getName()).partOfTheCity(partOfTheCity).build();

        StreetDTO streetDTO =
                StreetDTO.builder().id(id).name(streetRequest.getName()).partOfTheCity(partOfTheCity.getName()).build();

        when(streetService.getById(id)).thenReturn(Optional.of(street));
        when(partOfTheCityService.getPartOfTheCityByName(streetRequest.getPartOfTheCity())).thenReturn(partOfTheCity);
        when(streetService.save(street)).thenReturn(street);
        when(streetMapper.toDTO(street)).thenReturn(streetDTO);


        ResponseEntity<StreetDTO> responseEntity = streetController.update(id, streetRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(streetDTO, responseEntity.getBody());
    }

    @Test
    void update_should_return_not_found_when_street_with_given_id_not_exist() {
        final Long id = 1L;
        StreetRequest streetRequest =
                StreetRequest.builder().name("Test street 1").partOfTheCity(partOfTheCity.getName()).build();

        when(streetService.getById(id)).thenReturn(Optional.empty());

        ResponseEntity<StreetDTO> responseEntity = streetController.update(id, streetRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void delete_should_return_status_ok_when_entity_with_given_id_is_deleted() {
        final Long id = 1L;
        Street street = Street.builder().id(id).name("Test street 1").partOfTheCity(partOfTheCity).build();

        when(streetService.getById(id)).thenReturn(Optional.of(street));

        ResponseEntity<?> responseEntity = streetController.delete(id);

        verify(streetService).delete(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void delete_should_return_not_found_when_street_with_given_id_not_exist() {
        final Long id = 1L;

        when(streetService.getById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = streetController.delete(id);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}