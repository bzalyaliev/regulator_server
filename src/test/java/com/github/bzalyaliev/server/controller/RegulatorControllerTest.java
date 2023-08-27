package com.github.bzalyaliev.server.controller;

import com.github.bzalyaliev.regulator.Regulator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Collections;



import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegulatorControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Regulator regulator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSetTemperature() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/regulator/set")
                        .content(String.valueOf(25.0))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Temperature set successfully"));
    }

    @Test
    public void testCurrentTemperature_EmptyList() throws Exception {
        when(regulator.getTemperatureValues()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/regulator/current"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testClearTemperatures() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/regulator/clear"))
                .andExpect(status().isOk())
                .andExpect(content().string("Temperature history cleared"));
    }
}
