package com.example.apigateway.service;

import org.springframework.stereotype.Service;

import com.example.apigateway.payload.response.ApiResponse;
import com.example.apigateway.repository.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<Boolean>> verifyAccessToken(String token){
        return identityClient.verifyAccessToken(token);
    }
}
