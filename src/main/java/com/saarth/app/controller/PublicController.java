package com.saarth.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/greet")
    public String greet() {
        return "Khamma Ghani Sa";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Padharo Sa";
    }
}
