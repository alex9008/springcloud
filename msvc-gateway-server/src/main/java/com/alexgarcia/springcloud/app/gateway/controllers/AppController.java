package com.alexgarcia.springcloud.app.gateway.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AppController {

    @GetMapping("/authorized")
    public Map<String, String> authorized(@RequestParam Map<String, String> code) {

        return Map.of("code", code.get("code"));

    }

    @GetMapping("/logout")
    public Map<String, String> logout() {

        return Collections.singletonMap("logout", "OK");

    }
}
