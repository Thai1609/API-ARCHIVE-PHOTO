package com.michaelnguyen.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateRequest {

    private String name; // Tên Role (ví dụ: ADMIN, USER)

    private String description; // Mô tả của Role

    @NotNull(message = "Danh sách quyền không được để trống")
    @Size(min = 1, message = "Role phải có ít nhất một quyền")
    private Set<String> permissions; // Danh sách quyền (Permission dưới dạng String, ví dụ: ["READ", "WRITE"])


}
