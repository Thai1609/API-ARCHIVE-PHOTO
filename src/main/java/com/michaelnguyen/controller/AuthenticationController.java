package com.michaelnguyen.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		var result = authenticationService.authenticate(request);

		return ApiResponse.<AuthenticationResponse>builder().result(result).build();
	}

	@PostMapping("/signup")
	String createUser(@RequestBody @Valid UserCreationRequest request) throws MessagingException {
		userService.createUser(request);

		User user = iUserRepository.findByEmail(request.getEmail());

		// Tạo và gửi token xác thực
		String token = userService.createVerificationToken(user);
		// Gửi email xác thực chứa token
		String verificationUrl = "http://localhost:8080/api/auth/verify-register?token=" + token;

		// Nội dung HTML của email
		String htmlContent = "<!DOCTYPE html>" + "<html>" + "<head>" + "    <style>"
				+ "        .container { font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }"
				+ "        .button { background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; margin-top: 20px; border-radius: 5px; font-size: 16px; }"
				+ "        .button:hover { background-color: #45a049; }"
				+ "        .footer { margin-top: 30px; font-size: 12px; color: #999; }" + "    </style>" + "</head>"
				+ "<body>" + "    <div class=\"container\">" + "        <h2>Chào mừng bạn đến với [ARCHIVE PHOTO]</h2>"
				+ "        <p>Cảm ơn bạn đã đăng ký tài khoản với chúng tôi!</p>"
				+ "        <p>Vui lòng nhấn vào nút bên dưới để xác nhận địa chỉ email của bạn:</p>"
				+ "        <a href=\"" + verificationUrl + "\" class=\"button\">Xác nhận địa chỉ email</a>"
				+ "        <p>Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này.</p>"
				+ "        <div class=\"footer\">" + "            <p>Trân trọng,<br>Đội ngũ [Tên Website]</p>"
				+ "        </div>" + "    </div>" + "</body>" + "</html>";

		emailService.sendSimpleEmail(user.getEmail(), "Xác thực đăng ký tài khoản", htmlContent);

		return request.getEmail();
	}

	@PostMapping("/forgot")
	String authForgotPassword(@RequestParam String email) throws MessagingException {
		if (!iUserRepository.existsByEmail(email))
			throw new AppException(ErrorCode.EMAIL_NOT_EXIST);

		User user = iUserRepository.findByEmail(email);
		// Tạo và gửi token xác thực
		String token = userService.createVerificationToken(user);
		// Gửi email xác thực chứa token
		String verificationUrl = "http://localhost:8080/api/auth/verify-forgot?token=" + token;
		emailService.sendSimpleEmail(user.getEmail(), "Reset Your Password", verificationUrl);

		return "Vui long kiem tra gmail!";

	}

	@PostMapping("/reset-password")
	String authResetPassword(@RequestBody @Valid UserCreationRequest request) throws MessagingException {
		// Kiem tra mat khau da dang ky chua

		if (!iUserRepository.existsByEmail(request.getEmail()))
			throw new AppException(ErrorCode.EMAIL_NOT_EXIST);

		User user = iUserRepository.findByEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		iUserRepository.save(user);
		
		// Tạo và gửi mail thong bao
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
	public String verifyForgotPassword(@RequestParam("token") String token) {
		// Xác thực token
		Optional<VerificationToken> verificationTokenOpt = iVerificationTokenRepository.findByToken(token);

		if (verificationTokenOpt.isPresent()) {
			VerificationToken verificationToken = verificationTokenOpt.get();

			if (verificationToken.isExpired()) {
				return "<a href=\"http://localhost:3000/auth/login\">Quay lại trang chủ</a>";
			}

		}
		return "<a href=\"http://localhost:3000/auth/login\">Quay lại trang chủ</a>";
	}

}
