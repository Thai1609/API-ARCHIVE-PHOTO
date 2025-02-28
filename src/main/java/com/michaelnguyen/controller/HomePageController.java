package com.michaelnguyen.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {
    @GetMapping({"/", "home-page"})
    public ResponseEntity<String> homePage() {
        return ResponseEntity.ok("this is home page");
    }

}
