package com.michaelnguyen.controller;

import com.michaelnguyen.firebase.FirebaseStorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/storage")
public class FirebaseStorageController {

    private final FirebaseStorageService firebaseStorageService;

    public FirebaseStorageController(FirebaseStorageService firebaseStorageService) {
        this.firebaseStorageService = firebaseStorageService;
    }

    @GetMapping("/get-all-files")
    public List<String> getAllJpgFiles() {
        return firebaseStorageService.getAllJpgFiles();
    }
}
