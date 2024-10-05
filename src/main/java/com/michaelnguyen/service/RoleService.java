package com.michaelnguyen.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.michaelnguyen.dto.request.RoleRequest;
import com.michaelnguyen.dto.response.RoleResponse;
import com.michaelnguyen.mapper.IRoleMapper;
import com.michaelnguyen.repository.IPermissionRepository;
import com.michaelnguyen.repository.IRoleRepository;

@Service
public class RoleService {

	@Autowired
	IRoleRepository iRoleRepository;
	@Autowired
	IRoleMapper iRoleMapper;

	@Autowired
	IPermissionRepository iPermissionRepository;

	public RoleResponse create(RoleRequest request) {

		var role = iRoleMapper.toRole(request);

		var permissions = iPermissionRepository.findAllById(request.getPermissions());

		role.setPermissions(new HashSet<>(permissions));

		role = iRoleRepository.save(role);

		return iRoleMapper.toRoleResponse(role);

	}

	public List<RoleResponse> getAllRole() {
		return iRoleRepository.findAll().stream().map(iRoleMapper::toRoleResponse).toList();
	}

	public void deleteRole(String name) {
		iRoleRepository.deleteById(name);
	}
}
