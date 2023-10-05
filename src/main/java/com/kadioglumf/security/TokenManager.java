package com.kadioglumf.security;

import com.kadioglumf.model.UserDetailsImpl;
import com.kadioglumf.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final UserDetailsServiceImpl userDetailsService;

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${app.jwt.accessTokenSecret}")
    private String jwtAccessTokenSecret;
    @Value("${app.jwt.accessTokenExpirationMs}")
    private int jwtAccessTokenExpirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtAccessTokenSecret.getBytes());
    }

    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        String authorities = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtAccessTokenExpirationMs))
                .signWith(key)
                .compact();
    }

    public String removeBearerFromToken(String authToken) {
        if (StringUtils.hasText(authToken) && authToken.startsWith("Bearer "))
            authToken = authToken.substring(7);
        return authToken;
    }

    public boolean validateJwtToken(String authToken) {
        authToken = removeBearerFromToken(authToken);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String getEmailFromJwtToken(String token) {
        token = removeBearerFromToken(token);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public UserDetailsImpl getUserDetailsByJwt(String headerAuth) {
        String jwt = removeBearerFromToken(headerAuth);
        return loadUserByUsername(jwt);
    }

    private UserDetailsImpl loadUserByUsername(String jwt) {
        String email = getEmailFromJwtToken(jwt);
        return (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
    }
}

