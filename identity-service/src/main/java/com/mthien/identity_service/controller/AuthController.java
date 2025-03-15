package com.mthien.identity_service.controller;

import com.mthien.identity_service.payload.ApiResponse;
import com.mthien.identity_service.payload.auth.AuthResponse;
import com.mthien.identity_service.payload.auth.LoginRequest;
import com.mthien.identity_service.service.AuthService;
import com.mthien.identity_service.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    JwtService jwtService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ApiResponse.<AuthResponse>builder()
                .message("Login Successfully")
                .data(authService.login(loginRequest))
                .build();
    }

    @PostMapping("verify-refresh-token")
    public ApiResponse<Boolean> verifyRefresh(@RequestParam("token") String token) {
        return ApiResponse.<Boolean>builder()
                .message("Checked")
                .data(jwtService.verifyToken(token, true))
                .build();
    }

    @PostMapping("verify-access-token")
    public ApiResponse<Boolean> verifyAccess(@RequestParam("token") String token) {
        return ApiResponse.<Boolean>builder()
                .message("Checked")
                .data(jwtService.verifyToken(token, false))
                .build();
    }

    @PostMapping("refresh-token")
    public ApiResponse<AuthResponse> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return ApiResponse.<AuthResponse>builder()
                .message("Refresh Token")
                .data(AuthResponse.builder().accessToken(jwtService.refreshToken(refreshToken)).build())
                .build();
    }

    @PostMapping("logout")
    public ApiResponse<Void> logout(@RequestParam("refreshToken") String refreshToken) {
        jwtService.logout(refreshToken);
        return ApiResponse.<Void>builder()
                .message("Logout Successfully")
                .data(null)
                .build();
    }
}
