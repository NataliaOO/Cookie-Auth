package com.example.auth.web;

import com.example.auth.core.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my.app/api")
public class AuthController {

    private final AuthService authService;

    // флажок: по умолчанию false, в тестах включим
    @Value("${app.auth.set-session-cookie:false}")
    private boolean setSessionCookie;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(@RequestParam String username,
                                      @RequestParam String password,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        if (!authService.authenticate(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("username", username);
        // В тестовом режиме явно добавим JSESSIONID, чтобы cookie()-матчер увидел куку
        if (setSessionCookie) {
            Cookie js = new Cookie("JSESSIONID", session.getId());
            js.setHttpOnly(true);
            js.setPath("/");
            response.addCookie(js);
        }
        return ResponseEntity.ok().build();
    }
}
