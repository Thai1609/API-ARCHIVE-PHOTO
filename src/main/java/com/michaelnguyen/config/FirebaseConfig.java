package com.michaelnguyen.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

	@Bean
	FirebaseApp initializeFirebaseApp() throws IOException {
		FileInputStream serviceAccount = new FileInputStream(
				"src/main/resources/data-images-12d9b-firebase-adminsdk-pghiq-3131317fc4.json");

		FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setStorageBucket("data-images-12d9b.appspot.com").build();

		return FirebaseApp.initializeApp(options);
	}

}
