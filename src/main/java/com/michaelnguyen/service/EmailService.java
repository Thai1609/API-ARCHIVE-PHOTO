package com.michaelnguyen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Async("emailTaskExecutor")
	public void sendSimpleEmail(String toEmail, String subject, String body) throws MessagingException {
		try {
			// Tạo email với MimeMessage
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			
	        helper.setFrom("Archive Photo <michael.nguyen@gmail.com>");
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(body, true); // true để gửi nội dung HTML

			mailSender.send(message);

		} catch (AddressException ex) {
			// Handle invalid email address
			System.out.println("Invalid email address: " + ex.getMessage());
		}
	}
}
