package com.mthien.identity_service.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mthien.identity_service.config.AuthenticationRequestInterceptor;
import com.mthien.identity_service.payload.profile.ProfileRequest;
import com.mthien.identity_service.payload.profile.ProfileResponse;

@FeignClient(name = "profile-service", url = "${app.services.profile}", configuration = { AuthenticationRequestInterceptor.class })
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse createProfile(@RequestBody ProfileRequest request);
}
