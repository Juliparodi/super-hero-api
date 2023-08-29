package com.superhero.integration;

import static com.superhero.constants.OutputMessageConstants.DELETED;
import static com.superhero.constants.OutputMessageConstants.MESSAGE_OUTPUT;
import static com.superhero.factory.MockSuperHeroesFactory.getAllSuperHeroes;
import static com.superhero.factory.MockSuperHeroesFactory.getSuperHero;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.superhero.model.SuperHero;
import com.superhero.repository.SuperHeroRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SuperHeroDeleteIntegrationTest {

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
    public void whenDeletingExistingSuperHero_ThenReturnOKWithCongratsMessage() {
        SuperHero deletedSuperHero = getSuperHero(1).get();

        String outputMessage = String.format(MESSAGE_OUTPUT, deletedSuperHero.getId(), DELETED);

        mockMvc.perform(delete("/superheroes/hero/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(outputMessage));

    }

    @Test
    @SneakyThrows
    public void whenDeletingNotExistingSuperHero_ThenReturn404() {

        mockMvc.perform(delete("/superheroes/hero/10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

    }

}
