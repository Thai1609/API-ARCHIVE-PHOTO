package com.michaelnguyen.controller;

import com.michaelnguyen.dto.request.UserCreationRequest;
import com.michaelnguyen.dto.request.UserProfileUpdateRequest;
import com.michaelnguyen.dto.request.UserUpdateRequest;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.dto.response.UserResponse;
import com.michaelnguyen.service.UserProfileService;
import com.michaelnguyen.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    private final UserProfileService userProfileService;

    public UserController(UserService userService, UserProfileService userProfileService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
    }

    @PostMapping("/my-info")
    ApiResponse<?> getInfo(@RequestBody UserCreationRequest request) {
        return ApiResponse.builder().result(userService.getInfo(request)).build();
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

}