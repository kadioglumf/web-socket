package com.kadioglumf.security;

import com.kadioglumf.model.UserDetails;
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


    public UserDetails getUserDetailsByJwt(String headerAuth) {
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

    private UserDetails loadUserByUsername(String jwt) {
        HttpEntity<String> requestEntity = new HttpEntity<>(getSecureHttpHeader(jwt));
        //TODO check token
        UserDetails userDetails = new UserDetails(); //TODO new UserDetails with user
        userDetails.setEmail("test-fatih@gmail.com");
        userDetails.setAuth(Arrays.asList("ROLE_ADMIN", "ROLE_MODERATOR"));
        userDetails.setId(1L);
        userDetails.setRoles(Arrays.asList("ROLE_ADMIN", "ROLE_MODERATOR"));
        return userDetails;
    }

    public static HttpHeaders getSecureHttpHeader(String bearer) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.set(AUTHORIZATION_HEADER, bearer);
        return requestHeaders;
    }
}

