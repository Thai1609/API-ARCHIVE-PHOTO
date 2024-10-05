package com.michaelnguyen.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.michaelnguyen.dto.request.AuthenticationRequest;
import com.michaelnguyen.dto.request.UserCreationRequest;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.dto.response.AuthenticationResponse;
import com.michaelnguyen.dto.response.UserResponse;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.entity.VerificationToken;
import com.michaelnguyen.repository.IUserRepository;
import com.michaelnguyen.repository.IVerificationTokenRepository;
import com.michaelnguyen.service.AuthenticationService;
import com.michaelnguyen.service.EmailService;
import com.michaelnguyen.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	@Autowired
	AuthenticationService authenticationService;

	@Autowired
	private UserService userService;
	@Autowired
	private IVerificationTokenRepository iVerificationTokenRepository;
	@Autowired
	private IUserRepository iUserRepository;

	@Autowired
	private EmailService emailService;

	@PostMapping("/login")
	ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		var result = authenticationService.authenticate(request);

		return ApiResponse.<AuthenticationResponse>builder().result(result).build();
	}

	@PostMapping("/signup")
	ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
		UserResponse result = userService.createUser(request);

		User user = iUserRepository.findByEmail(request.getEmail());

		// Tạo và gửi token xác thực
		String token = userService.createVerificationToken(user);
		// Gửi email xác thực chứa token
		String verificationUrl = "http://localhost:8080/auth/verify?token=" + token;
		emailService.sendSimpleEmail(user.getEmail(), "Xác thực đăng ký tài khoản",
				"Nhấp vào liên kết sau để xác thực tài khoản của bạn: " + verificationUrl);

		return ApiResponse.<UserResponse>builder().result(result).build();
	}

	@PostMapping("/forgot")
	String authenticateForgot(@RequestParam("email") String email) {
		User user = iUserRepository.findByEmail(email);
		if (user != null) {

			// Tạo và gửi token xác thực
			String token = userService.createVerificationToken(user);
			// Gửi email xác thực chứa token
			String verificationUrl = "http://localhost:8080/auth/verify?token=" + token;
			emailService.sendSimpleEmail(user.getEmail(), "Reset Your Password: ", verificationUrl);
			return "Vui long kiem tra gmail!";

		} else {
			return "Email k ton tai!";
		}

	}

	@Transactional
	public boolean verifyToken(String token) {
		Optional<VerificationToken> verificationTokenOpt = iVerificationTokenRepository.findByToken(token);
		if (verificationTokenOpt.isPresent()) {
			VerificationToken verificationToken = verificationTokenOpt.get();

			if (verificationToken.isExpired()) {
				return false; // Token đã hết hạn
			}
			// Kích hoạt tài khoản người dùng
			User user = verificationToken.getUser();

			iUserRepository.updateEnabled(user.getId());

			// Xóa token sau khi sử dụng
			iVerificationTokenRepository.delete(verificationToken);

			return true;
		} else {
			return false; // Token không tồn tại
		}
	}

	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("token") String token) {
		// Xác thực token và kích hoạt tài khoản người dùng
		boolean isValid = verifyToken(token);
		if (isValid) {
			return "Tài khoản của bạn đã được xác thực thành công!";
		} else {
			return "Token xác thực không hợp lệ hoặc đã hết hạn.";
		}
	}

}
