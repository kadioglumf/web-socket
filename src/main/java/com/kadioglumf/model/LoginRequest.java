package com.kadioglumf.model;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
  @NotBlank private String email;

  @NotBlank private String password;
}
