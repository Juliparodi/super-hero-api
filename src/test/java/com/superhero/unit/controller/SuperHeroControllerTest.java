package com.superhero.unit.controller;

import com.superhero.controller.SuperHeroController;
import com.superhero.factory.MockSuperHeroesFactory;
import com.superhero.model.SuperHero;
import com.superhero.services.ISuperHeroService;
import com.superhero.utils.JsonConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class SuperHeroControllerTest {

    @MockBean
    private MockMvc mockMvc;

    @Mock
    private ISuperHeroService superHeroService;
    @InjectMocks
    private SuperHeroController superHeroController;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(superHeroController).build();
    }

    @Test
    public void testGetAllSuperHeroes() throws Exception {
        Mockito.when(superHeroService.getAllSuperHeroes()).thenReturn(MockSuperHeroesFactory.getAllSuperHeroes());

        mockMvc.perform(MockMvcRequestBuilders.get("/superheroes"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(JsonConverter.loadJsonFromFile("heroes.json")));
    }

    @Test
    public void testGetSuperHeroById() throws Exception {
        Long heroId = 2L;
        Mockito.when(superHeroService.getSuperHeroById(heroId))
            .thenReturn(MockSuperHeroesFactory.getSuperHero(heroId).get());

        mockMvc.perform(MockMvcRequestBuilders.get("/superheroes/{id}", heroId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(JsonConverter.loadJsonFromFile("heroe1.json")));
    }

    @Test
    public void testSearchSuperHeroesByName() throws Exception {
        String searchName = "man";
        Mockito.when(superHeroService.searchSuperHeroesByName(searchName))
            .thenReturn(MockSuperHeroesFactory.getSuperHeroContains(searchName));

        mockMvc.perform(MockMvcRequestBuilders.get("/superheroes/search")
                .param("name", searchName))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(JsonConverter.loadJsonFromFile("heroesMan.json")));
    }

}
