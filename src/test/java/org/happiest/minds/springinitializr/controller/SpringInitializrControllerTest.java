package org.happiest.minds.springinitializr.controller;

import org.happiest.minds.springinitializr.service.SpringInitializrService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SpringInitializrController.class)
class SpringInitializrControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private SpringInitializrService springInitializrService;

    @Test
    void testDownloadTemplate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/spring/initializr/zip")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.zip").exists());
    }
}