package com.mthien.identity_service.service;

import com.mthien.identity_service.exception.AppException;
import com.mthien.identity_service.exception.ErrorCode;
import com.mthien.identity_service.payload.auth.AuthResponse;
import com.mthien.identity_service.payload.auth.LoginRequest;
import com.mthien.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    JwtService jwtService;
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public AuthResponse login(LoginRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_INVALID));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

}
