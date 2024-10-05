package com.michaelnguyen.mapper;

import org.mapstruct.Mapper;

import com.michaelnguyen.dto.request.PermissionRequest;
import com.michaelnguyen.dto.response.PermissionResponse;
import com.michaelnguyen.entity.Permission;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {

	Permission toPermission(PermissionRequest request);

	PermissionResponse toPermissionResponse(Permission permission);

}
