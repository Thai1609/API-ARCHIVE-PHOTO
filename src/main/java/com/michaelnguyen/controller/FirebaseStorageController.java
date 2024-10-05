package com.michaelnguyen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.michaelnguyen.firebase.FirebaseStorageService;

@RestController
@RequestMapping("/api/storage")
public class FirebaseStorageController {

	@Autowired
	private FirebaseStorageService firebaseStorageService;

	@GetMapping("/get-all-files")
	public List<String> getAllJpgFiles() {
		return firebaseStorageService.getAllJpgFiles();
	}
}
