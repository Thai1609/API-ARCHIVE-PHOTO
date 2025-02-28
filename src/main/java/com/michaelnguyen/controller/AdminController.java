package com.michaelnguyen.controller;

import com.michaelnguyen.dto.request.UserUpdateRequest;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.dto.response.UserResponse;
import com.michaelnguyen.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

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
    public UserResponse updateRolesUser(@PathVariable Long id, @RequestParam String roles) {

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
