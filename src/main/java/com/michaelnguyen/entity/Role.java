package com.michaelnguyen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Role {
    @Id
    private String name;

    private String description;

    @ElementCollection(fetch = FetchType.EAGER) // Đánh dấu là tập hợp
    @CollectionTable(
            name = "role_permissions", // Tên bảng phụ để ánh xạ permissions
            joinColumns = @JoinColumn(name = "role_name") // Liên kết với Role qua khóa "role_name"
    )
    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng chuỗi
    private Set<Permission> permissions; // Danh sách quyền (enum)


}
