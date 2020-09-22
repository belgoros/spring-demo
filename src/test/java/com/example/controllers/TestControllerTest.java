package com.example.controllers;

import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.keycloak.ServletKeycloakAuthUnitTestingSupport;
import com.example.config.KeycloakSecurityConfig;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.security.Principal;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestController.class)
@Import({
        ServletKeycloakAuthUnitTestingSupport.UnitTestConfig.class,
        KeycloakSecurityConfig.class })
class TestControllerTest {
    @MockBean
    JwtDecoder jwtDecoder;

    @Autowired
    private MockMvcSupport mockMvc;

    @Test
    public void shouldBeAccessedWithoutToken() throws Exception {
        this.mockMvc
                .get("/test/anonymous").andExpect(status().isOk());
    }

    @Test
    @WithMockKeycloakAuth
    public void whenNoRolesSpecifiedThenSecuredRouteIsForbidden() throws Exception {
        mockMvc.get("/test/admin").andExpect(status().isForbidden());
    }

    @Test
    public void whenAdminRolesSpecifiedThenSecuredRouteIsOk() throws Exception {
        configureSecurityContext("employee2", "admin");

        mockMvc.get("/test/admin").andExpect(status().isOk());
    }

    private void configureSecurityContext(String username, String... roles) {
        final var principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        final var account = mock(OidcKeycloakAccount.class);
        when(account.getRoles()).thenReturn(Set.of(roles));
        when(account.getPrincipal()).thenReturn(principal);

        final var authentication = mock(KeycloakAuthenticationToken.class);
        when(authentication.getAccount()).thenReturn(account);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
