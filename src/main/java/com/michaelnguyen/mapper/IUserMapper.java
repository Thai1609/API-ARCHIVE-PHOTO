package com.michaelnguyen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.michaelnguyen.dto.request.UserCreationRequest;
import com.michaelnguyen.dto.request.UserUpdateRequest;
import com.michaelnguyen.dto.response.UserResponse;
import com.michaelnguyen.entity.User;

@Mapper(componentModel = "spring")
public interface IUserMapper {
	@Mapping(target = "roles", ignore = true)
	User toUser(UserCreationRequest request);

	UserResponse toUserResponse(User user);

	@Mapping(target = "roles", ignore = true)
	void updateUserByAdmin(@MappingTarget User user, UserUpdateRequest request);
}
