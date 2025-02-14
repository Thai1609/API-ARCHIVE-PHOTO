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

import com.michaelnguyen.dto.request.UserCreationRequest;
import com.michaelnguyen.dto.request.LoginRequest;
import com.michaelnguyen.dto.request.UserCreationRequest;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.dto.response.AuthenticationResponse;
import com.michaelnguyen.dto.response.UserResponse;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.entity.VerificationToken;
import com.michaelnguyen.error.AppException;
import com.michaelnguyen.error.ErrorCode;
import com.michaelnguyen.repository.IUserRepository;
import com.michaelnguyen.repository.IVerificationTokenRepository;
import com.michaelnguyen.service.AuthenticationService;
import com.michaelnguyen.service.EmailService;
import com.michaelnguyen.service.UserService;

import jakarta.mail.MessagingException;
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
	ApiResponse<AuthenticationResponse> authenticateWithEmail(@RequestBody LoginRequest request) {
		var result = authenticationService.authenticateWithEmail(request);

		return ApiResponse.<AuthenticationResponse>builder().result(result).build();
	}

	@PostMapping("/google")
	ApiResponse<AuthenticationResponse> authenticateWithGoogle(@RequestBody UserCreationRequest request) {
		var result = authenticationService.authenticateWithGoogle(request);

		return ApiResponse.<AuthenticationResponse>builder().result(result).build();
	}

	@PostMapping("/signup")
	UserResponse createUser(@RequestBody @Valid UserCreationRequest request) {
		return userService.createUser(request);
	}
	@PostMapping("/delete-user")
	UserResponse deleteUser(@RequestParam("userId") Long userId) {
		return userService.delete(userId);
	}

	@PostMapping("/forgot")
	String authForgotPassword(@RequestParam String email) throws MessagingException {

		Optional<User> optionalUser = iUserRepository.findByOptions(email, null, null);
		User user = optionalUser.orElse(null);

		if (user == null)
			throw new AppException(ErrorCode.EMAIL_NOT_EXIST);

		// Tạo và gửi token xác thực
		String token = userService.createVerificationToken(user);
		// Gửi email xác thực chứa token
		String verificationUrl = "http://localhost:8080/api/auth/verify-forgot?token=" + token;
		emailService.sendSimpleEmail(user.getEmail(), "Reset Your Password", verificationUrl);

		return "Vui long kiem tra gmail!";

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

	@GetMapping("/verify-register")
	public String verifyCreateAccount(@RequestParam("token") String token) {
		// Xác thực token và kích hoạt tài khoản người dùng
		boolean isValid = verifyToken(token);
		if (isValid) {
			return "Tài khoản của bạn đã được xác thực thành công! <a href=\"http://localhost:3000/auth/login\">Quay lại trang chủ</a>";
		} else {
			return "Token xác thực không hợp lệ hoặc đã hết hạn. <a href=\"http://localhost:3000/auth/login\">Quay lại trang chủ</a>";
		}
	}

	@GetMapping("/verify-forgot")
	public boolean verifyForgotPassword(@RequestParam("token") String token) {
		// Xác thực token
		Optional<VerificationToken> verificationTokenOpt = iVerificationTokenRepository.findByToken(token);

		if (verificationTokenOpt.isPresent()) {
			VerificationToken verificationToken = verificationTokenOpt.get();

			if (verificationToken.isExpired()) {
				
//				return "<a href=\"http://localhost:3000/auth/login\">Quay lại trang chủ</a>";
				return true;
			}

		}
		return false;
	}

}
