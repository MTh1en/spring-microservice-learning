package com.mthien.identity_service.mapper;

import com.mthien.identity_service.entity.Users;
import com.mthien.identity_service.payload.user.CreateUserRequest;
import com.mthien.identity_service.payload.user.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users createUser(CreateUserRequest request);

    UserResponse toUserResponse(Users user);
}
