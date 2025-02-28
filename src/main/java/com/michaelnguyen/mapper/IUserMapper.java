package com.michaelnguyen.mapper;

import com.michaelnguyen.dto.response.UserResponse;
import com.michaelnguyen.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    UserResponse toUserResponse(User user);

}
