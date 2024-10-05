package com.michaelnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.michaelnguyen.dto.request.UserUpdateRequest;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.dto.response.UserResponse;
import com.michaelnguyen.service.UserService;

@RestController
@RequestMapping("api/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	@GetMapping("/get-all-users")
	ApiResponse<?> getAllUsers() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();

		System.out.println("UserName: " + authentication.getName());
		authentication.getAuthorities()
				.forEach(grantedAuth -> System.out.println("Role: " + grantedAuth.getAuthority()));

		return ApiResponse.builder().result(userService.getAllUser()).build();
	}

	@GetMapping("/get-user/{id}")
	ApiResponse<?> getUserById(@PathVariable Long id) {
		return ApiResponse.builder().result(userService.getUserById(id)).build();
	}

	/*
	 * User management
	 */
	@PutMapping("/update-user/{id}")
	public UserResponse updateUserByAdmin(@PathVariable Long id, @RequestBody UserUpdateRequest request) {

		return userService.updateUserByAdmin(id, request);
	}

	@PutMapping("/update-role-user/{id}")
	public UserResponse updateRolesUser(@PathVariable Long id,@RequestParam String roles) {

		return userService.updateRolesUser(id, roles);
	}

	@DeleteMapping("/delete-user/{id}")
	public void deleteUser(@PathVariable Long id) {
		userService.delete(id);
	}

	/*
	 * Product management
	 */
}
