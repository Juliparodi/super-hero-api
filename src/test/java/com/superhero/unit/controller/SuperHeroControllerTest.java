package com.superhero.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.superhero.controller.SuperHeroController;
import com.superhero.model.SuperHero;
import com.superhero.services.ISuperHeroService;
import com.superhero.utils.JsonConverter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;

import static com.superhero.constants.OutputMessageConstants.*;
import static com.superhero.factory.SuperHeroesFactory.*;
import static com.superhero.utils.JsonConverter.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SuperHeroControllerTest {

    @MockitoBean
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
        when(superHeroService.getAllSuperHeroes()).thenReturn(getAllSuperHeroesWithIdAndDates());

        mockMvc.perform(get("/superheroes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(JsonConverter.loadJsonFromFile("heroes.json")));
    }

    @Test
    @SneakyThrows
    void testGetSuperHeroById() {
        Long heroId = 3L;
        when(superHeroService.getSuperHeroById(heroId))
            .thenReturn(getSuperHero(heroId).get());

       mockMvc.perform(get("/superheroes/{id}", heroId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(JsonConverter.loadJsonFromFile("heroe3.json")));
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
            .andExpect(content().json(JsonConverter.loadJsonFromFile("heroes-man.json")));
    }

    @Test
    @SneakyThrows
    void testCreateSuperHero() {
        SuperHero inputSuperHero = newSuperHero();

        when(superHeroService.createSuperHero(any())).thenReturn(inputSuperHero);

        String outputMessage = String.format(MESSAGE_OUTPUT, inputSuperHero.getId(), CREATED);

       mockMvc.perform(post("/superheroes/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConverter.loadJsonFromFile("new-heroe.json")))
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

        when(superHeroService.updateSuperHero(any(), anyLong())).thenReturn(inputSuperHero);

        String outputMessage = String.format(MESSAGE_OUTPUT, toBeUpdatedSuperHero.getId(), UPDATED);

        mockMvc.perform(put("/superheroes/hero/{id}", heroId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConverter.loadJsonFromFile("heroe3.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(outputMessage)));


        verify(superHeroService).updateSuperHero(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void testDeleteSuperHero() {
        Long heroId = 1L;
        SuperHero deletedSuperHero = getSuperHero(heroId).get();

        String outputMessage = String.format(MESSAGE_OUTPUT, deletedSuperHero.getId(), DELETED);

        mockMvc.perform(delete("/superheroes/hero/{id}", heroId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(toJson(outputMessage)));

        verify(superHeroService).deleteSuperHero(anyLong());
    }

}
