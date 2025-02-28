package com.michaelnguyen.dto.response;

import com.michaelnguyen.entity.Role;
import com.michaelnguyen.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String email;

    private UserProfile userProfile;

    private List<Role> roles;

}
