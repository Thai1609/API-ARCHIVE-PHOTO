package com.michaelnguyen.controller;

import com.michaelnguyen.dto.request.RoleCreateRequest;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.entity.Role;
import com.michaelnguyen.service.RoleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {


    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    ApiResponse<Role> create(@RequestBody RoleCreateRequest request) {
        return ApiResponse.<Role>builder().result(roleService.createRole(request)).build();
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
