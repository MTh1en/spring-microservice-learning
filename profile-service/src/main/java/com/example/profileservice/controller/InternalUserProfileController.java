package com.example.profileservice.controller;

import com.example.profileservice.payload.UserProfileRequest;
import com.example.profileservice.payload.UserProfileResponse;
import com.example.profileservice.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;
    @PostMapping("/internal/users")
    UserProfileResponse createProfile(@RequestBody UserProfileRequest request) {
        return userProfileService.createProfile((request));
    }
}
