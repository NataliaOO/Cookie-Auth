package com.example.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("given no session when GET /auth then 403")
    void givenNoSession_whenAuth_then403() throws Exception {
        // Given: нет сессии

        // When: вызываем /auth без cookie
        mockMvc.perform(get("/my.app/api/auth"))
                // Then: 403 Forbidden
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("given session for user1 when GET /auth then returns permissions p1,p2,p3")
    void givenSessionWithUser1_whenAuth_thenReturnPermissions() throws Exception {
        // Given: есть сессия с username=user1
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "user1");

        // When: вызываем /auth с этой сессией
        mockMvc.perform(get("/my.app/api/auth").session(session))
                // Then: 200 и JSON-массив полномочий
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$", hasItems("p1","p2","p3")));
    }

    @Test
    @DisplayName("given session with unknown user when GET /auth then 403")
    void givenUnknownUserInSession_whenAuth_then403() throws Exception {
        // Given: сессия с несуществующим пользователем
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "ghost");

        // When
        mockMvc.perform(get("/my.app/api/auth").session(session))
                // Then
                .andExpect(status().isForbidden())
                .andExpect(cookie().doesNotExist("JSESSIONID"));
    }
}
