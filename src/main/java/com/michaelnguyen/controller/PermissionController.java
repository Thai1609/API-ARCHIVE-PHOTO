package com.michaelnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.michaelnguyen.dto.request.PermissionRequest;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.dto.response.PermissionResponse;
import com.michaelnguyen.service.PermissionService;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

	@Autowired
	PermissionService permissionService;

	@PostMapping("/create")
	ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
		return ApiResponse.<PermissionResponse>builder().result(permissionService.create(request)).build();
	}

	@GetMapping("/get-all")
	ApiResponse<?> getAllPermission() {
		return ApiResponse.builder().result(permissionService.getAllPermission()).build();
	}

	@DeleteMapping("/{name}")
	public void deletePermission(@PathVariable String name) {
		permissionService.deletePermission(name);
	}
}
