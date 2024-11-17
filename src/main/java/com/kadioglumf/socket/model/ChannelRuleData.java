package com.kadioglumf.socket.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ChannelRuleData {
    private String name;
    private Set<String> allowedRoles;
}
