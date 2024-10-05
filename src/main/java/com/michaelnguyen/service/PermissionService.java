package com.michaelnguyen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.michaelnguyen.dto.request.PermissionRequest;
import com.michaelnguyen.dto.response.PermissionResponse;
import com.michaelnguyen.entity.Permission;
import com.michaelnguyen.mapper.IPermissionMapper;
import com.michaelnguyen.repository.IPermissionRepository;

@Service
public class PermissionService {

	@Autowired
	IPermissionRepository iPermissionRepository;
	@Autowired
	IPermissionMapper iPermissionMapper;

	public PermissionResponse create(PermissionRequest request) {

		Permission permission = iPermissionMapper.toPermission(request);
		permission = iPermissionRepository.save(permission);

		return iPermissionMapper.toPermissionResponse(permission);

	}

	public List<PermissionResponse> getAllPermission() {
		var permission = iPermissionRepository.findAll();
		return permission.stream().map(iPermissionMapper::toPermissionResponse).toList();
	}

	public void deletePermission(String name) {
		iPermissionRepository.deleteById(name);
	}
}
