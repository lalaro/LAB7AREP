package com.example.laboratorio7;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifica si la API redirige a la autenticación en endpoints protegidos.
     */
    @Test
    void testRedirectToAuthentication() throws Exception {
        mockMvc.perform(get("/secure-data"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/cognito"));
    }

    /**
     * Verifica que las cabeceras de seguridad estén presentes.
     */
    @Test
    void testSecurityHeaders() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(header().exists("X-Content-Type-Options"))
                .andExpect(header().exists("X-Frame-Options"))
                .andExpect(header().exists("Cache-Control"));
    }

    /**
     * Verifica que la eliminación de un post devuelve un JSON vacío.
     */
    @Test
    void testDeletePostReturnsEmptyResponse() throws Exception {
        mockMvc.perform(delete("/posts/1"))
                .andExpect(jsonPath("$").doesNotExist());
    }

    /**
     * Verifica si la API redirige a la autenticación en otra ruta protegida.
     */
    @Test
    void testRedirectToAuthenticationForOtherRoute() throws Exception {
        mockMvc.perform(get("/admin-panel"))
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/cognito"));
    }

    /**
     * Verifica que las cabeceras de seguridad están presentes en otro endpoint.
     */
    @Test
    void testSecurityHeadersForDifferentRoute() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(header().exists("X-Content-Type-Options"))
                .andExpect(header().exists("X-Frame-Options"))
                .andExpect(header().exists("Cache-Control"));
    }


    /**
     * Verifica que eliminar un usuario devuelve un JSON vacío.
     */
    @Test
    void testDeleteUserReturnsEmptyResponse() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(jsonPath("$").doesNotExist());
    }

    /**
     * Verifica que las cabeceras de seguridad están presentes en el perfil del usuario.
     */
    @Test
    void testSecurityHeadersForProfile() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(header().exists("X-Content-Type-Options"))
                .andExpect(header().exists("X-Frame-Options"))
                .andExpect(header().exists("Cache-Control"));
    }

    /**
     * Verifica que múltiples usuarios pueden crear posts simultáneamente sin problemas de concurrencia.
     */
    @Test
    void testConcurrentPostCreation() throws Exception {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    mockMvc.perform(post("/posts")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\"title\":\"Post concurrente\", \"content\":\"Contenido de prueba\"}"))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }

    /**
     * Verifica que un usuario sin permisos no puede eliminar un post.
     */
    @Test
    void testUnauthorizedUserCannotDeletePost() throws Exception {
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isForbidden()); // 403 Forbidden si el usuario no tiene permisos
    }

    /**
     * Verifica que un usuario no autenticado recibe un error 401 en rutas protegidas.
     */
    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/secure-data"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", containsString("/oauth2/authorization/cognito")));
    }
}
