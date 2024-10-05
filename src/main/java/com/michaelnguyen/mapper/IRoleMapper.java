package com.michaelnguyen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.michaelnguyen.dto.request.RoleRequest;
import com.michaelnguyen.dto.response.RoleResponse;
import com.michaelnguyen.entity.Role;

@Mapper(componentModel = "spring")
public interface IRoleMapper {

	@Mapping(target = "permissions", ignore = true)
	Role toRole(RoleRequest request);

	RoleResponse toRoleResponse(Role role);

}
