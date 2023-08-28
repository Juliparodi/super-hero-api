package com.superhero.unit.controller;

import static com.superhero.constants.OutputMessageConstants.CREATED;
import static com.superhero.constants.OutputMessageConstants.DELETED;
import static com.superhero.constants.OutputMessageConstants.MESSAGE_OUTPUT;
import static com.superhero.constants.OutputMessageConstants.UPDATED;
import static com.superhero.factory.MockSuperHeroesFactory.getAllSuperHeroes;
import static com.superhero.factory.MockSuperHeroesFactory.getSuperHero;
import static com.superhero.factory.MockSuperHeroesFactory.getSuperHeroContains;
import static com.superhero.factory.MockSuperHeroesFactory.inputUpdatedSuperHero;
import static com.superhero.factory.MockSuperHeroesFactory.newSuperHero;
import static com.superhero.utils.JsonConverter.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.superhero.constants.ExceptionConstants;
import com.superhero.constants.OutputMessageConstants;
import com.superhero.controller.SuperHeroController;
import com.superhero.exception.SuperHeroNotFoundException;
import com.superhero.factory.MockSuperHeroesFactory;
import com.superhero.model.SuperHero;
import com.superhero.services.ISuperHeroService;
import com.superhero.utils.JsonConverter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
            .registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders
            .standaloneSetup(superHeroController)
            .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
            .build();

    }

    @Test
    @SneakyThrows
    void testGetAllSuperHeroes() {
        when(superHeroService.getAllSuperHeroes()).thenReturn(getAllSuperHeroes());

        mockMvc.perform(get("/superheroes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(JsonConverter.loadJsonFromFile("heroes.json")));
    }

    @Test
    @SneakyThrows
    void testGetSuperHeroById() {
        Long heroId = 2L;
        when(superHeroService.getSuperHeroById(heroId))
            .thenReturn(getSuperHero(heroId).get());

       mockMvc.perform(get("/superheroes/{id}", heroId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(JsonConverter.loadJsonFromFile("heroe2.json")));
    }

    @Test
    @SneakyThrows
    void testSearchSuperHeroesByName() {
        String searchName = "man";
        when(superHeroService.searchSuperHeroesByName(searchName))
            .thenReturn(getSuperHeroContains(searchName));

        mockMvc.perform(get("/superheroes/search")
                .param("name", searchName))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(JsonConverter.loadJsonFromFile("heroesMan.json")));
    }

    @Test
    @SneakyThrows
    void testCreateSuperHero() {
        SuperHero inputSuperHero = newSuperHero();

        when(superHeroService.createSuperHero(inputSuperHero)).thenReturn(inputSuperHero);

        String outputMessage = String.format(MESSAGE_OUTPUT, inputSuperHero.getName(), CREATED);


        mockMvc.perform(post("/superheroes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConverter.loadJsonFromFile("newHeroe.json")))
            .andExpect(status().isOk())
            .andExpect(content().json(toJson(outputMessage)));

        verify(superHeroService).createSuperHero(any());
    }

    @Test
    @SneakyThrows
    void testUpdateSuperHero() {
        Long heroId = 3L;
        SuperHero inputSuperHero = inputUpdatedSuperHero();
        SuperHero toBeUpdatedSuperHero = getSuperHero(heroId).get();
        toBeUpdatedSuperHero.setDescription("Wonder Woman, also known as Diana Prince, is an iconic superheroine and a founding member of the Justice League");

        when(superHeroService.updateSuperHero(toBeUpdatedSuperHero, heroId)).thenReturn(inputSuperHero);

        String outputMessage = String.format(MESSAGE_OUTPUT, toBeUpdatedSuperHero.getName(), UPDATED);
        mockMvc.perform(put("/superheroes/{id}", heroId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConverter.loadJsonFromFile("heroe2.json")))
            .andExpect(status().isOk())
            .andExpect(content().json(toJson(outputMessage)));


        verify(superHeroService).updateSuperHero(any(), any());
    }

    @Test
    @SneakyThrows
    void testDeleteSuperHero() {
        Long heroId = 1L;
        SuperHero deletedSuperHero = getSuperHero(heroId).get();

        when(superHeroService.deleteSuperHero(deletedSuperHero, heroId)).thenReturn(deletedSuperHero);

        String outputMessage = String.format(MESSAGE_OUTPUT, deletedSuperHero.getName(), DELETED);

        mockMvc.perform(delete("/superheroes/{id}", heroId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(toJson(outputMessage)));

        verify(superHeroService).deleteSuperHero(any(), any());
    }

}
