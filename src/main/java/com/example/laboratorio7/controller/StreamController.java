package com.example.laboratorio7.controller;

import com.example.laboratorio7.model.Post;
import com.example.laboratorio7.service.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class StreamController {

    @Autowired
    private StreamService streamService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OidcUser principal, Model model) {
        // Agregar información del usuario autenticado al modelo
        if (principal != null) {
            model.addAttribute("username", principal.getClaim("name"));
            model.addAttribute("email", principal.getEmail());
        }

        // Obtener todos los posts y añadirlos al modelo
        List<Post> posts = streamService.getAllPosts();
        model.addAttribute("posts", posts);

        // Añadir un post vacío para el formulario
        model.addAttribute("newPost", new Post());

        return "home";
    }

    @PostMapping("/post")
    public String addPost(@ModelAttribute Post newPost, @AuthenticationPrincipal OidcUser principal) {
        // Establecer el autor como el nombre del usuario autenticado
        if (principal != null) {
            // Intenta obtener el nombre de diferentes claims comunes
            Object nameClaim = principal.getClaim("name");
            if (nameClaim != null) {
                newPost.setAuthor(nameClaim.toString());
            } else if (principal.getEmail() != null) {
                // Usa el email como alternativa
                newPost.setAuthor(principal.getEmail());
            } else if (principal.getPreferredUsername() != null) {
                // Usa el username preferido como otra alternativa
                newPost.setAuthor(principal.getPreferredUsername());
            } else {
                // Si todo lo demás falla, usa un valor predeterminado
                newPost.setAuthor("Usuario " + principal.getSubject().substring(0, 6));
            }
        } else {
            newPost.setAuthor("Anonymous");
        }

        // Guardar el post
        streamService.savePost(newPost);

        // Redirigir de vuelta a la página principal
        return "redirect:/";
    }
}