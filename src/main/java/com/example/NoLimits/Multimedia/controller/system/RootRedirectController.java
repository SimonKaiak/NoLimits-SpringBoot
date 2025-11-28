package com.example.NoLimits.Multimedia.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootRedirectController {

    @GetMapping("/")
    public String redirectToSwagger() {
        // Redirige la raíz "/" hacia Swagger UI
        return "redirect:/doc/swagger-ui.html";
    }
}