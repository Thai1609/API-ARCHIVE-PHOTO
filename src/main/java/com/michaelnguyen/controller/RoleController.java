package com.michaelnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.michaelnguyen.dto.request.RoleRequest;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.dto.response.RoleResponse;
import com.michaelnguyen.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	@Autowired
	RoleService roleService;

	@PostMapping("/create")
	ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
		return ApiResponse.<RoleResponse>builder().result(roleService.create(request)).build();
	}

	@GetMapping("/get-all")
	ApiResponse<?> getAllRole() {
		return ApiResponse.builder().result(roleService.getAllRole()).build();
	}

	@DeleteMapping("/{name}")
	public void deleteRole(@PathVariable String name) {
		roleService.deleteRole(name);
	}
}
