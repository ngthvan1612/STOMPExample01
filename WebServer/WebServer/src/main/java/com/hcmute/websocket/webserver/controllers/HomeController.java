package com.hcmute.websocket.webserver.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/home")
public class HomeController {
    public HomeController() {

    }

    @GetMapping
    public Map<String, Object> index() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Welcome to -->> HCMUTE");
        return map;
    }
}
