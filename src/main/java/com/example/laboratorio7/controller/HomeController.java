package com.example.laboratorio7.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.Objects;

@RestController
public class HomeController {



    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        new SecurityContextLogoutHandler().logout(request, null, null);
        return "redirect:https://tudominio.auth.us-east-1.amazoncognito.com/logout?client_id=TU_CLIENT_ID&logout_uri=http://localhost:8080";
    }

    @GetMapping("/juanito")
    public Map<String, String> juanito() {
        return Map.of(
                "name", "andres",
                "email", "idiota",
                "claims", "absoluto"
        );
    }




}
