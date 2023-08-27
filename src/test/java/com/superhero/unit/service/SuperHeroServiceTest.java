package com.superhero.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.superhero.factory.MockSuperHeroesFactory;
import com.superhero.model.SuperHero;
import com.superhero.repository.SuperHeroRepository;
import com.superhero.services.impl.SuperHeroServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    public void testGetAllSuperHeroes() {
        List<SuperHero> mockSuperHeroes = MockSuperHeroesFactory.getAllSuperHeroes();

        given(superHeroRepository.findAll()).willReturn(mockSuperHeroes);

        List<SuperHero> result = superHeroService.getAllSuperHeroes();

        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("Batman", result.get(3).getName());
    }

    @Test
    public void testGetSuperHeroById() {
        Long heroId = 1L;
        Optional<SuperHero> superHero = MockSuperHeroesFactory.getSuperHero(heroId);

        given(superHeroRepository.findById(any())).willReturn(superHero);

        SuperHero result = superHeroService.getSuperHeroById(heroId);

        assertNotNull(result);
        assertEquals(heroId, result.getId());
    }

    @Test
    public void testSearchSuperHeroesByName() {
        String searchName = "de";
        List<SuperHero> mockSuperHeroes = MockSuperHeroesFactory.getSuperHeroContains(searchName);

        given(superHeroRepository.findByNameContainingIgnoreCase(searchName)).willReturn(mockSuperHeroes);

        List<SuperHero> result = superHeroService.searchSuperHeroesByName(searchName);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Wonder Woman", result.get(1).getName());

    }

}
