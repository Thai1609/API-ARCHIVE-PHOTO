package com.michaelnguyen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.michaelnguyen.dto.request.UserProfileCreationRequest;
import com.michaelnguyen.dto.request.UserProfileUpdateRequest;
import com.michaelnguyen.dto.response.UserProfileResponse;
import com.michaelnguyen.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface IUserProfileMapper {

	UserProfile toUserProfile(UserProfileCreationRequest request);

	UserProfileResponse toUserProfileResponse(UserProfile userProfile);

	void updateUserProfile(@MappingTarget UserProfile userProfile, UserProfileUpdateRequest request);

}
