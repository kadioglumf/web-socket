package com.kadioglumf.utils;

import com.kadioglumf.model.UserDetailsImpl;
import com.kadioglumf.socket.model.enums.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class UserThreadContext {

  public static String getUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    try {
      if (authentication == null || authentication.getPrincipal() == null) {
        return null;
      }
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      return userDetails.getEmail();
    } catch (ClassCastException c) {
      log.error("ClassCastException User not found");
    }
    return null;
  }

  public static Long getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    try {
      if (authentication == null || authentication.getPrincipal() == null) {
        return null;
      }
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      return userDetails.getId();
    } catch (ClassCastException c) {
      log.error("ClassCastException User not found");
    }
    return null;
  }

  public static UserDetailsImpl getUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getPrincipal() == null) {
      return null;
    }
    return (UserDetailsImpl) authentication.getPrincipal();
  }

  public static boolean isAdmin() {
    var user = getUserDetails();
    return user != null
        && user.getRoles().stream().anyMatch(r -> RoleType.ROLE_ADMIN.name().equals(r));
  }
}
