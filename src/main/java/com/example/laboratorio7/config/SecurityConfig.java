package com.example.laboratorio7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String COGNITO_JWK_SET_URI = "https://cognito-idp.us-east-1.amazonaws.com/us-east-1_I55X45CTg/.well-known/jwks.json";

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(COGNITO_JWK_SET_URI).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").authenticated() // Protege la raíz
                        .anyRequest().authenticated() // Protege todas las rutas
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/", true) // Después del login, redirige a "/"
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // Redirige a /login tras cerrar sesión
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
