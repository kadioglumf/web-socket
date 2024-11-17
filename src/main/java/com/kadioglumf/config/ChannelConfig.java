package com.kadioglumf.config;

import com.kadioglumf.repository.ChannelRepository;
import com.kadioglumf.socket.model.ChannelRuleData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class ChannelConfig {

    private final ChannelRepository channelRepository;

    @Bean
    public ChannelConfigData channelConfigData() {
        var channels = channelRepository.findAll();
        return ChannelConfigData
                .builder()
                .channels(channels
                        .stream()
                        .map(c -> ChannelRuleData.builder().name(c.getName()).allowedRoles(c.getRoles()).build())
                        .collect(Collectors.toList()))
                .build();
    }
}
