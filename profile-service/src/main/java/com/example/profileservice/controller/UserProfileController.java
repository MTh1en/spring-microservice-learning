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
public class UserProfileController {
    UserProfileService userProfileService;
    @PostMapping("/users")
    UserProfileResponse createProfile(@RequestBody UserProfileRequest request) {
        return userProfileService.createProfile((request));
    }

    @GetMapping("/users/{profileId}")
    UserProfileResponse getProfile(@PathVariable String profileId) {
        return userProfileService.getProfile(profileId);
    }

    @DeleteMapping("/users/{profileId}")
    void deleteProfile(@PathVariable String profileId) {
        userProfileService.deleteProfile(profileId);
    }
}
