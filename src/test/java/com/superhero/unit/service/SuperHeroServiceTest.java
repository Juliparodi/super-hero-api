package com.superhero.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.superhero.factory.MockSuperHeroesFactory;
import com.superhero.model.SuperHero;
import com.superhero.repository.SuperHeroRepository;
import com.superhero.services.impl.SuperHeroServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
        assertEquals("Wonder Woman", result.get(3).getName());
    }

    @Test
    public void testGetSuperHeroById() {
        Long heroId = 1L;
        Optional<SuperHero> superHero = MockSuperHeroesFactory.getSuperHero(heroId);

        when(superHeroRepository.findById(1L)).thenReturn(superHero);

        SuperHero result = superHeroService.getSuperHeroById(heroId);

        assertNotNull(result);
        assertEquals(heroId, result.getId());
    }

    @Test
    public void testSearchSuperHeroesByName() {
        String searchName = "de";
        List<SuperHero> mockSuperHeroes = MockSuperHeroesFactory.getSuperHeroContains(searchName);

        when(superHeroRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(mockSuperHeroes);

        List<SuperHero> result = superHeroService.searchSuperHeroesByName(searchName);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Wonder Woman", result.get(1).getName());

    }

}
