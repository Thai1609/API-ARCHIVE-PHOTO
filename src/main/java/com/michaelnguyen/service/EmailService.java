package com.michaelnguyen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendSimpleEmail(String toEmail, String subject, String body) {
		try {
			// Validate email address
			InternetAddress emailAddr = new InternetAddress(toEmail);
			emailAddr.validate();

			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toEmail);
			message.setSubject(subject);
			message.setText(body);
			message.setFrom("thaixalem367@gmail.com");

			mailSender.send(message);

		} catch (AddressException ex) {
			// Handle invalid email address
			System.out.println("Invalid email address: " + ex.getMessage());
		}
	}
}
