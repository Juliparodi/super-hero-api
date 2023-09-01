package com.superhero.integration;

import static com.superhero.constants.OutputMessageConstants.CREATED;
import static com.superhero.constants.OutputMessageConstants.MESSAGE_OUTPUT;
import static com.superhero.constants.SecurityConstants.BEARER;
import static com.superhero.constants.SecurityConstants.JWT_HEADER;
import static com.superhero.factory.AuthenticationFactory.createAuthRoleAdmin;
import static com.superhero.factory.AuthenticationFactory.createAuthRoleRead;
import static com.superhero.factory.SuperHeroesFactory.getAllSuperHeroes;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.superhero.repository.SuperHeroRepository;
import com.superhero.utils.AuthenticationUtil;
import com.superhero.utils.JsonConverter;
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
public class SuperHeroPostIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SuperHeroRepository superHeroRepository;

    @Autowired
    private AuthenticationUtil authenticationUtil;

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
    public void whenCreateNewSuperHero_ThenReturnOKWithCongratsMessage() {
        String outputMessage = String.format(MESSAGE_OUTPUT, 8, CREATED);
        mockMvc.perform(post("/superheroes/hero")
                .header(JWT_HEADER, BEARER + authenticationUtil.createToken(createAuthRoleAdmin()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConverter.loadJsonFromFile("new-heroe.json")))
            .andExpect(status().isOk())
            .andExpect(content().string(outputMessage));
    }

    @Test
    @SneakyThrows
    public void whenCreateNewSuperHeroWithNameNull_ThenReturn400BadRequest() {
        mockMvc.perform(post("/superheroes/hero")
                .header(JWT_HEADER, BEARER + authenticationUtil.createToken(createAuthRoleAdmin()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConverter.loadJsonFromFile("new-heroe-null-name.json")))
            .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void whenCreateNewSuperHeroWithoutTokenHeader_ThenReturn401() {

        mockMvc.perform(post("/superheroes/hero")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());

    }

    @Test
    @SneakyThrows
    public void whenCreateNewSuperHeroWithAUserWithUserRole_ThenReturn401() {

        mockMvc.perform(post("/superheroes/hero")
                .header(JWT_HEADER, BEARER + authenticationUtil.createToken(createAuthRoleRead()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());

    }



}
