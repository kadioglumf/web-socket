package com.kadioglumf.socket.model;

import lombok.Data;

import java.util.Set;

@Data
public class ChannelRuleData {
    private String name;
    private Set<String> allowedRoles;
}
