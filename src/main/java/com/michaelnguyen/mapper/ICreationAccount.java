package com.michaelnguyen.mapper;

import org.mapstruct.Mapper;

import com.michaelnguyen.dto.request.LoginRequest;
import com.michaelnguyen.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface ICreationAccount {
	UserCreationRequest toLoginRequest(LoginRequest request);
}
