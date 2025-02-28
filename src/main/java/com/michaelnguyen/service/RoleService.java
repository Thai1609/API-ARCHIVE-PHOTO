package com.michaelnguyen.service;

import com.michaelnguyen.dto.request.RoleCreateRequest;
import com.michaelnguyen.entity.Permission;
import com.michaelnguyen.entity.Role;
import com.michaelnguyen.repository.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final IRoleRepository iRoleRepository;


    public RoleService(IRoleRepository iRoleRepository) {
        this.iRoleRepository = iRoleRepository;
    }

    public Role createRole(RoleCreateRequest request) {
        // Kiểm tra Role đã tồn tại chưa
        if (iRoleRepository.findById(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Role " + request.getName() + " đã tồn tại.");
        }

        // Convert danh sách permissions từ String -> Enum Permission
        Set<Permission> permissions = request.getPermissions().stream().map(permissionName -> {
            try {
                return Permission.valueOf(String.valueOf(permissionName));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Permission không tồn tại: " + permissionName);
            }
        }).collect(Collectors.toSet());

        // Tạo Role mới
        Role role = Role.builder().name(request.getName()).description(request.getDescription()).permissions(permissions).build();

        // Lưu vào database
        return iRoleRepository.save(role);
    }


    public List<Role> getAllRole() {
        return iRoleRepository.findAll();
    }

    public void deleteRole(String name) {
        iRoleRepository.deleteById(name);
    }
}
