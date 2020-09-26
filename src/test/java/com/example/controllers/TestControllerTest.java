package com.example.controllers;

import com.tngtech.keycloakmock.api.KeycloakMock;
import com.tngtech.keycloakmock.api.TokenConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TestControllerTest {

    static KeycloakMock keycloakMock = new KeycloakMock(8080, "demo");

    @BeforeAll
    static void setUp() {
        keycloakMock.start();
    }

    @AfterAll
    static void tearDown() {
        keycloakMock.stop();
    }

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

    @Test
    public void shouldGrantAccessToAdminsWithValidToken() throws Exception {
        TokenConfig tokenConfig = aTokenConfig()
                .withPreferredUsername("employee2")
                .withResourceRole("springboot-microservice", "admin")
                .build();
        String accessToken = keycloakMock.getAccessToken(tokenConfig);

        mockMvc.perform(get("/test/admin")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGrantAccessToUsersWithValidToken() throws Exception {
        TokenConfig tokenConfig = aTokenConfig()
                .withPreferredUsername("employee1")
                .withResourceRole("springboot-microservice", "user")
                .build();
        String accessToken = keycloakMock.getAccessToken(tokenConfig);

        mockMvc.perform(get("/test/user")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }
}
