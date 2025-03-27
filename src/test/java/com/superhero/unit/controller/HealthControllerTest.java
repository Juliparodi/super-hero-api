package com.superhero.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.superhero.config.kubernetes.CustomHealthIndicator;
import com.superhero.controller.HealthController;
import com.superhero.utils.JsonConverter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class HealthControllerTest {

  @MockBean
  private MockMvc mockMvc;
  @Mock
  private CustomHealthIndicator customHealthIndicator;
  @InjectMocks
  private HealthController healthController;


  @BeforeEach
  void setUp() {
    ObjectMapper objectMapper = new ObjectMapper()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
        .registerModule(new JavaTimeModule());

    mockMvc = MockMvcBuilders
        .standaloneSetup(healthController)
        .build();

  }

  @Test
  @SneakyThrows
  void whenAppIsUp_thenReturnHealthOk() {
    when(customHealthIndicator.health()).thenReturn(Health.up().build());

    mockMvc.perform(get("/actuator/custom-health"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(JsonConverter.loadJsonFromFile("healthOk.json")));
  }

  @Test
  @SneakyThrows
  void whenAppIsDown_thenReturnHealthNotOk() {
    when(customHealthIndicator.health()).thenReturn(Health.down().build());

    mockMvc.perform(get("/actuator/custom-health"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(JsonConverter.loadJsonFromFile("healthDown.json")));
  }
}
