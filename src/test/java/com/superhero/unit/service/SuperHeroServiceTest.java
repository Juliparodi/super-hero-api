package com.superhero.unit.service;

import static com.superhero.factory.SuperHeroesFactory.createNewSuperHeroe;
import static com.superhero.factory.SuperHeroesFactory.getAllSuperHeroesWithIdAndDates;
import static com.superhero.factory.SuperHeroesFactory.getSuperHero;
import static com.superhero.factory.SuperHeroesFactory.getSuperHeroContains;
import static com.superhero.factory.SuperHeroesFactory.inputUpdatedSuperHero;
import static com.superhero.factory.SuperHeroesFactory.newSuperHero;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.superhero.exception.SuperHeroNotFoundException;
import com.superhero.model.SuperHero;
import com.superhero.repository.SuperHeroRepository;
import com.superhero.services.impl.SuperHeroServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SuperHeroServiceTest {

    @Mock
    private SuperHeroRepository superHeroRepository;

    @InjectMocks
    private SuperHeroServiceImpl superHeroService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSuperHeroes() {
        List<SuperHero> mockSuperHeroes = getAllSuperHeroesWithIdAndDates();

        given(superHeroRepository.findAll()).willReturn(mockSuperHeroes);

        List<SuperHero> result = superHeroService.getAllSuperHeroes();

        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("Green Lantern", result.get(3).getName());
    }

    @Test
    void testGetSuperHeroById() {
        Long heroId = 1L;
        Optional<SuperHero> superHero = getSuperHero(heroId);

        given(superHeroRepository.findById(any())).willReturn(superHero);

        SuperHero result = superHeroService.getSuperHeroById(heroId);

        assertNotNull(result);
        assertEquals(heroId, result.getId());
    }

    @Test
    void testSearchSuperHeroesByName() {
        String searchName = "de";
        List<SuperHero> mockSuperHeroes = getSuperHeroContains(searchName);

        given(superHeroRepository.findByNameContainingIgnoreCase(searchName)).willReturn(mockSuperHeroes);

        List<SuperHero> result = superHeroService.searchSuperHeroesByName(searchName);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Wonder Woman", result.get(1).getName());

    }

    @Test
    void testGetSuperHeroById_ThrowsExceptionWhenNotFound() {
        long heroId = 1L;

        given(superHeroRepository.findById(heroId)).willReturn(Optional.empty());

        assertThrows(SuperHeroNotFoundException.class, () -> superHeroService.getSuperHeroById(heroId));
    }

    @Test
    void testSearchSuperHeroesByName_ThrowsExceptionWhenNotFound() {
        String heroName = "man";

        given(superHeroRepository.findByNameContainingIgnoreCase(heroName)).willReturn(Collections.emptyList());

        assertThrows(SuperHeroNotFoundException.class, () -> superHeroService.searchSuperHeroesByName(heroName));
    }

    @Test
    void testGetAllSuperHeroes_ThrowsExceptionWhenNotFound() {
        given(superHeroRepository.findAll()).willReturn(Collections.emptyList());

        assertThrows(SuperHeroNotFoundException.class, () -> superHeroService.getAllSuperHeroes());
    }

    @Test
    void testCreateSuperHero() {
        SuperHero inputSuperHero = newSuperHero();
        SuperHero createdSuperHero = createNewSuperHeroe(inputSuperHero);

        given(superHeroRepository.save(any())).willReturn(createdSuperHero);

        SuperHero result = superHeroService.createSuperHero(inputSuperHero);

        assertEquals(createdSuperHero, result);
    }

    @Test
    void testUpdateSuperHero() {
        Long heroId = 1L;
        SuperHero inputSuperHero = inputUpdatedSuperHero();
        SuperHero foundSuperHero = getSuperHero(heroId).get();
        foundSuperHero.setDescription("Superman possesses immense strength, enabling him to lift enormous objects and face formidable foes.");

        given(superHeroRepository.findById(heroId)).willReturn(Optional.of(foundSuperHero));
        given(superHeroRepository.save(any())).willReturn(foundSuperHero);

        SuperHero result = superHeroService.updateSuperHero(inputSuperHero, heroId);

        assertEquals(foundSuperHero, result);
    }

    @Test
    void testDeleteSuperHero() {
        Long heroId = 1L;
        SuperHero superHeroToDelete = getSuperHero(heroId).get();

        given(superHeroRepository.findById(heroId)).willReturn(Optional.of(superHeroToDelete));

        superHeroService.deleteSuperHero(heroId);

        verify(superHeroRepository, times(1)).deleteById(heroId);
    }

}
