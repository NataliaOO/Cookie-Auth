package com.example.auth;

import com.example.auth.core.AuthService;
import com.example.auth.core.AuthorizationService;
import com.example.auth.web.AuthController;
import com.example.auth.web.AuthorizationController;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AuthController.class, AuthorizationController.class})
class AuthFlowMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    @DisplayName("login -> auth: возвращает JSESSIONID и список прав")
    void login_then_auth_ok_with_cookie() throws Exception {
        // Given
        when(authService.authenticate("user1", "pass1")).thenReturn(true);
        when(authorizationService.permissionsFor("user1"))
                .thenReturn(Optional.of(List.of("p1","p2","p3")));

        // When: логинимся
        MvcResult res = mockMvc.perform(get("/my.app/api/login")
                        .param("username","user1")
                        .param("password","pass1"))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("username", "user1"))
                .andReturn();

        // Then: сервер вернул СЕССИЮ
        MockHttpSession session = (MockHttpSession) res.getRequest().getSession(false);
        assertNotNull(session);

        mockMvc.perform(get("/my.app/api/auth").session(session))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"p1\",\"p2\",\"p3\"]"));
    }
}
