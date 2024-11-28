package com.kadioglumf.service;

import com.kadioglumf.constant.CacheConstants;
import com.kadioglumf.dto.request.AddChannelRequestDto;
import com.kadioglumf.dto.request.DeleteChannelRequestDto;
import com.kadioglumf.dto.response.ChannelResponseDto;
import com.kadioglumf.dto.response.UserPreferencesResponseDto;
import com.kadioglumf.model.Channel;
import com.kadioglumf.model.UserChannelPreferences;
import com.kadioglumf.repository.ChannelRepository;
import com.kadioglumf.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChannelService {
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;

  public List<ChannelResponseDto> getAll() {
    var channels = channelRepository.findAll();
    return channels.stream()
        .map(
            c ->
                ChannelResponseDto.builder()
                    .name(c.getName())
                    .roles(c.getRoles())
                    .userPreferences(
                        c.getUserChannelPreferences().stream()
                            .map(
                                p ->
                                    UserPreferencesResponseDto.builder()
                                        .userId(p.getUserId())
                                        .isSubscribed(p.isSubscribed())
                                        .channel(p.getChannel().getName())
                                        .build())
                            .collect(Collectors.toList()))
                    .build())
        .collect(Collectors.toList());
  }

  public ChannelResponseDto getByName(String name) {
    var channel = channelRepository.findByName(name);
    return channel
        .map(
            value ->
                ChannelResponseDto.builder()
                    .name(value.getName())
                    .roles(value.getRoles())
                    .userPreferences(
                        value.getUserChannelPreferences().stream()
                            .map(
                                p ->
                                    UserPreferencesResponseDto.builder()
                                        .userId(p.getUserId())
                                        .isSubscribed(p.isSubscribed())
                                        .channel(p.getChannel().getName())
                                        .build())
                            .collect(Collectors.toList()))
                    .build())
        .orElse(null);
  }

  @Transactional
  public void addChannel(AddChannelRequestDto request) {
    channelRepository.save(
        Channel.builder().name(request.getName()).roles(request.getRoles()).build());
  }

  @Transactional
  public void deleteChannel(DeleteChannelRequestDto request) {
    channelRepository.deleteByName(request.getName());
  }

  @Transactional
  public void subscribeChannel(String channel, Long userId) {
    channelRepository
        .findByName(channel)
        .ifPresent(
            channelModel -> {
              var user = userRepository.findById(userId);
              if (user.isPresent()
                  && channelModel.getRoles().stream()
                      .anyMatch(r -> user.get().getRoles().contains(r))) {
                if (channelModel.getUserChannelPreferences().stream()
                    .noneMatch(c -> c.getUserId().equals(userId))) {
                  var preference =
                      UserChannelPreferences.builder()
                          .channel(channelModel)
                          .userId(userId)
                          .isSubscribed(true)
                          .build();
                  channelModel.getUserChannelPreferences().add(preference);
                  channelRepository.save(channelModel);
                } else if (channelModel.getUserChannelPreferences().stream()
                    .anyMatch(c -> c.getUserId().equals(userId) && !c.isSubscribed())) {
                  channelModel.getUserChannelPreferences().stream()
                      .filter(c -> c.getUserId().equals(userId))
                      .findFirst()
                      .ifPresent(p -> p.setSubscribed(true));
                  channelRepository.save(channelModel);
                }
              }
            });
  }

  @Transactional
  public void unsubscribeChannel(String channel, Long userId) {
    channelRepository
        .findByName(channel)
        .ifPresent(
            channelModel -> {
              if (channelModel.getUserChannelPreferences().stream()
                  .anyMatch(c -> c.getUserId().equals(userId))) {
                channelModel.getUserChannelPreferences().stream()
                    .filter(c -> c.getUserId().equals(userId))
                    .findFirst()
                    .ifPresent(p -> p.setSubscribed(false));
                channelRepository.save(channelModel);
              }
            });
  }

  @Caching(
      evict = {
        @CacheEvict(value = CacheConstants.CHANNEL_CACHE_VALUE, allEntries = true),
        @CacheEvict(value = CacheConstants.USER_CHANNEL_CACHE_VALUE, allEntries = true)
      })
  public void refreshChannels() {}
}
