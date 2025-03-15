package com.mthien.identity_service.service;

import com.mthien.identity_service.entity.InvalidatedToken;
import com.mthien.identity_service.entity.Users;
import com.mthien.identity_service.exception.AppException;
import com.mthien.identity_service.exception.ErrorCode;
import com.mthien.identity_service.repository.InvalidatedTokenRepository;
import com.mthien.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    @Value("${access.token.signature}")
    private String ACCESS_SIGNATURE;
    @Value("${refresh.token.signature}")
    private String REFRESH_SIGNATURE;

    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    public String generateAccessToken(Users user) {
        try {
            //Khai báo phần header của jwt
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

            //Khai báo phần payload của jwt
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getId())
                    .issuer("mthien")
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli()))
                    .claim("token_type", "access")
                    .claim("scope", buildScope(user))
                    .build();

            Payload payload = new Payload(jwtClaimsSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);

            //Kí token
            jwsObject.sign(new MACSigner(ACCESS_SIGNATURE.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot generate token", e);
            throw new RuntimeException(e);
        }
    }

    public String generateRefreshToken(Users user) {
        try {
            //Khai báo phần header của jwt
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

            //Khai báo phần payload của jwt
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getId())
                    .issuer("mthien")
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("token_type", "refresh")
                    .build();

            Payload payload = new Payload(jwtClaimsSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);

            //Kí token
            jwsObject.sign(new MACSigner(REFRESH_SIGNATURE.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot generate token", e);
            throw new RuntimeException(e);
        }
    }

    public String refreshToken(String refreshToken) {
        try {
            if (!verifyToken(refreshToken, true)) {
                throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
            String id = signedJWT.getJWTClaimsSet().getSubject();
            Users newUser = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
            return generateAccessToken(newUser);
        } catch (ParseException e) {
            throw new AppException(ErrorCode.DECODE_ERROR);
        }
    }

    public void logout(String refreshToken) {
        try {
            if (!verifyToken(refreshToken, true)) {
                throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            if (jwtId.isEmpty()) {
                throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jwtId)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public Boolean verifyToken(String token, boolean isRefresh) {
        try {
            boolean isValid = true;
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = (isRefresh)
                    ? new MACVerifier(REFRESH_SIGNATURE.getBytes())
                    : new MACVerifier(ACCESS_SIGNATURE.getBytes());

            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified = signedJWT.verify(verifier);
            if (!(verified && expiryTime.after(new Date()))) isValid = false;
            if (isRefresh) {
                if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) isValid = false;
            }
            return isValid;
        } catch (JOSEException | ParseException e) {
            return false;
        }
    }

    private String buildScope(Users user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions()
                            .forEach(permission -> stringJoiner.add(permission.getName()));
            });
        }
        return stringJoiner.toString();
    }


}
