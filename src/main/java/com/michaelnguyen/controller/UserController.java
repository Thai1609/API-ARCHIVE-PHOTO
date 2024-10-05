package com.michaelnguyen.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.michaelnguyen.dto.request.UserProfileUpdateRequest;
import com.michaelnguyen.dto.request.UserUpdateRequest;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.dto.response.UserProfileResponse;
import com.michaelnguyen.dto.response.UserResponse;
import com.michaelnguyen.service.UserProfileService;
import com.michaelnguyen.service.UserService;

@RestController
@RequestMapping("api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserProfileService userProfileService;

	@GetMapping("/my-info")
	ApiResponse<?> getInfo() {
		return ApiResponse.builder().result(userService.getInfo()).build();
	}

	@PutMapping("/{id}/update-password")
	public UserResponse updateUserByUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
		return userService.updateUserByUser(id, request);
	}

	@GetMapping("/{id}/profile")
	ApiResponse<?> getUserProfileById(@PathVariable Long id) throws IOException {
		return ApiResponse.builder().result(userProfileService.getUserProfileById(id)).build();
	}

	@PutMapping("/{id}/profile/update")
	public UserProfileResponse updateUserProfile(@PathVariable Long id, @RequestBody UserProfileUpdateRequest request)
			throws IOException {
		return userProfileService.updateUserProfile(id, request);
	}

	@PostMapping("/profile/{id}/uploadImage")
	public UserProfileResponse uploadImgProfile(@PathVariable Long id,
			@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
		return userProfileService.uploadImgProfile(id, imageFile);
	}

//	@GetMapping("/export/excel")
//	public void exportToExcel(HttpServletResponse response) throws IOException {
//		response.setContentType("application/octet-stream");
//		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//		String currentDateTime = dateFormatter.format(new Date());
//
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
//		response.setHeader(headerKey, headerValue);
//
//		List<UserResponse> listUsers = userService.getAllUser();
//
//		UserExcelExporter excelExporter = new UserExcelExporter(listUsers);
//
//		excelExporter.export(response);
//	}
}