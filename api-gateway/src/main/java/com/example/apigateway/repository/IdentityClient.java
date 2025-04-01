package com.example.apigateway.repository;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

import com.example.apigateway.payload.response.ApiResponse;

import reactor.core.publisher.Mono;

@Repository
public interface IdentityClient {
    @PostExchange(url= "/auth/verify-access-token", contentType= MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<Boolean>> verifyAccessToken(@RequestParam("token") String token);
}
