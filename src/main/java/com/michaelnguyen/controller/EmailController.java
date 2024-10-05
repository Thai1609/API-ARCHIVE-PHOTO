package com.michaelnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.michaelnguyen.service.EmailService;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;

	@GetMapping("/sendEmail")
	public String sendEmail(@RequestParam String toEmail, @RequestParam String subject, @RequestParam String body) {
		try {
			emailService.sendSimpleEmail(toEmail, subject, body);
			return "Email sent successfully";
		} catch (Exception e) {
			return "Failed to send email: " + e.getMessage();
		}
	}
}
