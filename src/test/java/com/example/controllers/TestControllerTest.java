package com.example.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldBeAccessedWithoutToken() throws Exception {
        this.mockMvc
                .perform(get("/test/anonymous")).andExpect(status().isOk());
    }

    @Test
    public void whenNoRolesSpecifiedThenSecuredRouteIsForbidden() throws Exception {
        mockMvc.perform(get("/test/admin")).andExpect(status().isUnauthorized());
    }
}
