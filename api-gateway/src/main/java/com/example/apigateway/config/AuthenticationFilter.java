package com.example.apigateway.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import com.example.apigateway.payload.response.ApiResponse;
import com.example.apigateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    String[] PUBLIC_ENDPOINTS = { "/identity/auth/.*",
            "/identity/users/registration",
            "/notification/email/send" };

    @Value("${app.api-prefix}")
    @NonFinal
    private String API_PREFIX;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter...");
        // If public endpoint => pass filter
        if (isPublicEndpoint(exchange.getRequest())) {
            return chain.filter(exchange);
        }
        // Get token form authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get("Authorization");
        if (CollectionUtils.isEmpty(authHeader)) {
            return unthenticated(exchange.getResponse());
        }

        String token = authHeader.getFirst().replace("Bearer ", "");
        log.info("Token {}", token);
        // Verify token
        identityService.verifyAccessToken(token).subscribe(response -> {
            log.info("Response {}", response.getData());
        });

        return identityService.verifyAccessToken(token).flatMap(response -> {
            if (response.getData()) {
                return chain.filter(exchange);
            } else {
                return unthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> unthenticated(exchange.getResponse()));
        // Delegate identity servicef
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(PUBLIC_ENDPOINTS)
                .anyMatch(endpoint -> request.getURI().getPath().matches(API_PREFIX + endpoint));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1401)
                .message("UNAUTHENTICATED")
                .data(null)
                .build();
        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
