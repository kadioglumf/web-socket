package com.kadioglumf.security;

import com.kadioglumf.model.UserDetailsImpl;
import com.kadioglumf.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class TokenManager {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    public UserDetailsImpl getUserDetailsByJwt(String headerAuth) {
        String jwt;
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            jwt = headerAuth.substring(7);
        }
        else if (StringUtils.hasText(headerAuth)) {
            jwt = headerAuth;
        }
        else {
            return null;
        }
        return loadUserByUsername(jwt);
    }

    private UserDetailsImpl loadUserByUsername(String jwt) {
        String email = jwtUtils.getEmailFromJwtToken(jwt);
        return (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
    }
}

