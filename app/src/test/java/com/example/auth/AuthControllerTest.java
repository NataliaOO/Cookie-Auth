package com.example.auth;

import com.example.auth.core.AuthService;
import com.example.auth.web.AuthController;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@TestPropertySource(properties = "app.auth.set-session-cookie=true")
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Test
    @DisplayName("Валидный логин возвращает 200 OK и устанавливает куки")
    void givenValidCredentials_whenLogin_thenReturns200AndSessionCookie_v1() throws Exception {
        // Given: пользователь user1 с паролем pass1 есть в системе
        when(authService.authenticate("user1", "pass1")).thenReturn(true);

        // When: выполняем GET-запрос /my.app/api/login
        mockMvc.perform(get("/my.app/api/login")
                        .param("username", "user1")
                        .param("password", "pass1"))
                // Then: ожидаем код 200 и установку JSESSIONID куки
                .andExpect(status().isOk())
                .andExpect(cookie().exists("JSESSIONID"));
    }

    @Test
    @DisplayName("Валидный логин возвращает 200 OK и устанавливает куки")
    void givenValidCredentials_whenLogin_thenReturns200AndSessionCookie() throws Exception {
        // Given: пользователь user1 с паролем pass1 есть в системе
        when(authService.authenticate("user1", "pass1")).thenReturn(true);

        // When: выполняем GET-запрос /my.app/api/login
        mockMvc.perform(get("/my.app/api/login")
                        .param("username", "user1")
                        .param("password", "pass1"))
                // Then: ожидаем код 200 и установку JSESSIONID куки
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("username", "user1"))
                .andReturn();
    }

    @Test
    @DisplayName("Неверный логин возвращает 403 Forbidden")
    void givenInvalidCredentials_whenLogin_thenReturns403() throws Exception {
        // Given: неверные учётные данные

        // When: выполняем запрос с ними
        mockMvc.perform(get("/my.app/api/login")
                        .param("username", "baduser")
                        .param("password", "wrongpass"))
                // Then: ожидаем 403 Forbidden
                .andExpect(status().isForbidden());
    }
}
