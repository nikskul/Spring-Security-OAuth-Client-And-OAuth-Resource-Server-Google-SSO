package ru.nikskul.sandbox.spring.security.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/api")
    @PreAuthorize("authenticated()")
    public String index(Model model) {
        return "index";
    }

}