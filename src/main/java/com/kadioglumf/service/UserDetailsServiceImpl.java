package com.kadioglumf.service;

import com.kadioglumf.model.UserDetailsImpl;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    if ("admin@mail.com".equals(email)) {
      List<String> roles = Arrays.asList("ROLE_ADMIN");
      List<GrantedAuthority> authorities =
          roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

      return new UserDetailsImpl(
          1L, email, new BCryptPasswordEncoder().encode("admin"), authorities, roles, true);
    }
    List<String> roles = Arrays.asList("ROLE_USER");
    List<GrantedAuthority> authorities =
        roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    return new UserDetailsImpl(
        2L, email, new BCryptPasswordEncoder().encode("user"), authorities, roles, true);
  }
}
