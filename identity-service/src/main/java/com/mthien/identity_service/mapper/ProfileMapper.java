package com.mthien.identity_service.mapper;

import com.mthien.identity_service.payload.profile.ProfileRequest;
import com.mthien.identity_service.payload.user.CreateUserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
     ProfileRequest toProfileRequest(CreateUserRequest request);
}
