package com.kadioglumf.config;

import com.kadioglumf.socket.model.ChannelRuleData;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class ChannelConfig {
    private List<ChannelRuleData> channels;
}

