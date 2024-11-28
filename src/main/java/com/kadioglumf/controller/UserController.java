package com.kadioglumf.controller;

import com.kadioglumf.model.JwtResponse;
import com.kadioglumf.model.LoginRequest;
import com.kadioglumf.model.UserDetailsImpl;
import com.kadioglumf.security.TokenManager;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

  private final AuthenticationManager authenticationManager;
  private final TokenManager tokenManager;

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    String jwt = tokenManager.generateJwtToken(userDetails);

    JwtResponse jwtResponse = new JwtResponse();
    jwtResponse.setToken(jwt);
    jwtResponse.setId(userDetails.getId());
    jwtResponse.setEmail(userDetails.getEmail());
    jwtResponse.setRoles(roles);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return ResponseEntity.ok(jwtResponse);
  }
}
