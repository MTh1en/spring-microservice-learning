package com.mthien.identity_service.config;

import com.mthien.identity_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${access.token.signature}")
    private String ACCESS_TOKEN_SIGNATURE;
    private final JwtService jwtService;
    private NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        if (!jwtService.verifyToken(token, false)) {
            throw new BadCredentialsException("INVALID TOKEN");
        }
        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec keySpec = new SecretKeySpec(ACCESS_TOKEN_SIGNATURE.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(keySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
