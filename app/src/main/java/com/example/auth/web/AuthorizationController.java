package com.example.auth.web;

import com.example.auth.core.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my.app/api")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/auth")
    public ResponseEntity<?> auth(HttpServletRequest request) {
        // не создаём новую сессию (важно для 403 когда её нет)
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(403).build();
        }
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(403).build();
        }

        return authorizationService.permissionsFor(username)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(403).build());
    }
}
