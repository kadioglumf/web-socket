package com.kadioglumf.service;

import com.kadioglumf.config.ChannelConfigData;
import com.kadioglumf.dto.request.AddChannelRequestDto;
import com.kadioglumf.dto.request.DeleteChannelRequestDto;
import com.kadioglumf.dto.request.UpdateChannelRequestDto;
import com.kadioglumf.dto.response.ChannelResponseDto;
import com.kadioglumf.model.Channel;
import com.kadioglumf.repository.ChannelRepository;
import com.kadioglumf.socket.model.ChannelRuleData;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;

    @Cacheable("websocketChannels")
    public List<ChannelResponseDto> fetchAll() {
        var channels = channelRepository.findAll();
        return channels
                .stream()
                .map(c -> ChannelResponseDto.builder().id(c.getId())
                        .name(c.getName())
                        .roles(c.getRoles())
                        .build())
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "websocketChannels", allEntries = true)
    public void refreshChannels() {
    }

    @CacheEvict(value = "websocketChannels", allEntries = true)
    @Transactional
    public void updateChannel(UpdateChannelRequestDto request) {
        var channel = channelRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Channel not found"));
        channel.setName(request.getName());
        channel.setRoles(request.getRoles());
        channelRepository.save(channel);
    }

    @CacheEvict(value = "websocketChannels", allEntries = true)
    @Transactional
    public void addChannel(AddChannelRequestDto request) {
        channelRepository.save(Channel.builder().name(request.getName()).roles(request.getRoles()).build());
    }

    @CacheEvict(value = "websocketChannels", allEntries = true)
    @Transactional
    public void deleteChannel(DeleteChannelRequestDto request) {
        channelRepository.deleteByName(request.getName());
    }
}
