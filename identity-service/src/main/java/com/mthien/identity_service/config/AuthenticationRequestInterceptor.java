package com.mthien.identity_service.config;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(feign.RequestTemplate requestTemplate) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        var authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");
        log.info("authHeader: {}", authHeader);
        if (StringUtils.hasText(authHeader)) {
            requestTemplate.header("Authorization", authHeader);
        } else {
            log.warn("Authorization header is missing in the request");
        }
    }
}
