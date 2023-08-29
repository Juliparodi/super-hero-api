package com.superhero.integration;

import static com.superhero.factory.MockSuperHeroesFactory.getAllSuperHeroes;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.superhero.repository.SuperHeroRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class SuperHeroGetIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SuperHeroRepository superHeroRepository;

    @BeforeEach
    public void setUp() {
        superHeroRepository.saveAll(getAllSuperHeroes());
    }

    @AfterEach
    public void afterAll() {
        superHeroRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void whenGetSuperHeroes_thenReturnSuperHeroes() {

        mockMvc.perform(get("/superheroes"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(4))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Spider-Man"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[3].description").value("Guardian of Sector 2814 with a power ring"));

    }

    @Test
    @SneakyThrows
    public void whenGetSuperHeroById_thenReturnSuperHero() {
        mockMvc.perform(get("/superheroes/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Superman"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("The Man of Steel"));
    }

    @Test
    @SneakyThrows
    public void whenGetSuperHeroByText_thenReturnSuperHeroes() {
        mockMvc.perform(get("/superheroes/search?name=man"))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Spider-Man"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value("Amazonian princess with super strength"));
    }

    @Test
    @SneakyThrows
    public void whenGetSuperHeroByIdNotFound_thenReturn404() {

        mockMvc.perform(get("/superheroes/11"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void whenIdIsNull_thenReturn400() {

        mockMvc.perform(get("/superheroes/null"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void whenGetSuperHeroByTextNotFound_thenReturn404() {

        mockMvc.perform(get("/superheroes/search?name=hi"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void whenTextIsEmpty_thenReturn400() {

        mockMvc.perform(get("/superheroes/search?name= "))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
