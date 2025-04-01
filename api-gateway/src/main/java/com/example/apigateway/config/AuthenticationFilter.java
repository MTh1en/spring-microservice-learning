package com.example.apigateway.config;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import com.example.apigateway.service.IdentityService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered{
    IdentityService identityService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter...");
        //Get token form authorization header 
        List<String> authHeaeder = exchange.getRequest().getHeaders().get("Authorization");
        if (CollectionUtils.isEmpty(authHeaeder)) {
           return unthenticated(exchange.getResponse());
        }

        String token = authHeaeder.getFirst().replace("Bearer ", "");
        log.info("Token {}", token);
        //Verify token
        identityService.verifyAccessToken(token).subscribe(response -> {
            log.info("Response {}", response);
        });;
        //Delegate identity service 

        //

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unthenticated(ServerHttpResponse response) {
        String body = "UNAUTHENTICATED";
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(
            Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
