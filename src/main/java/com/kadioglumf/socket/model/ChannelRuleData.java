package com.kadioglumf.socket.model;

import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelRuleData {
  private String name;
  private Set<String> allowedRoles;
}
