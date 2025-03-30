package com.example.profileservice.mapper;

import com.example.profileservice.entity.UserProfile;
import com.example.profileservice.payload.UserProfileRequest;
import com.example.profileservice.payload.UserProfileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile (UserProfileRequest request);
    UserProfileResponse toUserProfileResponse (UserProfile entity);
}
