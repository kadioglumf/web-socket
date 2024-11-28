package com.kadioglumf.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String email;
  private List<String> roles;
}
