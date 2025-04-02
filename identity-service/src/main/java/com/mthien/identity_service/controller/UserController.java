package com.mthien.identity_service.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthien.identity_service.payload.ApiResponse;
import com.mthien.identity_service.payload.user.CreateUserRequest;
import com.mthien.identity_service.payload.user.UserResponse;
import com.mthien.identity_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("registration")
    public ApiResponse<UserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return ApiResponse.<UserResponse>builder()
                .message("Create User successfully")
                .data(userService.createUser(createUserRequest))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .message("Get User successfully")
                .data(userService.getUserById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        log.info("Authenticate: {}", SecurityContextHolder.getContext().getAuthentication().getName());
        return ApiResponse.<List<UserResponse>>builder()
                .message("Get User successfully")
                .data(userService.getAllUsers())
                .build();
    }
}
