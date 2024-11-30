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
import org.springframework.cache.annotation.Cacheable;
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

  @Cacheable(
      value = CacheConstants.CHANNEL_CACHE_VALUE,
      key = "T(com.kadioglumf.constant.CacheConstants).CHANNEL_CACHE_KEY + '_' + #name")
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
    Channel channel = new Channel();
    channel.setName(request.getName());
    channel.setRoles(request.getRoles());
    channelRepository.save(channel);
  }

  @CacheEvict(
      value = CacheConstants.CHANNEL_CACHE_VALUE,
      key = "T(com.kadioglumf.constant.CacheConstants).CHANNEL_CACHE_KEY + '_' + #request.name")
  @Transactional
  public void deleteChannel(DeleteChannelRequestDto request) {
    channelRepository.deleteByName(request.getName());
  }

  @Caching(
      evict = {
        @CacheEvict(
            value = CacheConstants.CHANNEL_CACHE_VALUE,
            key = "T(com.kadioglumf.constant.CacheConstants).CHANNEL_CACHE_KEY + '_' + #channel"),
        @CacheEvict(
            value = CacheConstants.USER_CHANNEL_PREFERENCES_CACHE_VALUE,
            key = "T(com.kadioglumf.constant.CacheConstants).USER_ID_KEY + '_' + #userId"),
        @CacheEvict(
            value = CacheConstants.USER_CHANNEL_PREFERENCES_CACHE_VALUE,
            key = "T(com.kadioglumf.constant.CacheConstants).CHANNEL_NAME_KEY + '_' + #channel")
      })
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

  @Caching(
      evict = {
        @CacheEvict(
            value = CacheConstants.CHANNEL_CACHE_VALUE,
            key = "T(com.kadioglumf.constant.CacheConstants).CHANNEL_CACHE_KEY + '_' + #channel"),
        @CacheEvict(
            value = CacheConstants.USER_CHANNEL_PREFERENCES_CACHE_VALUE,
            key = "T(com.kadioglumf.constant.CacheConstants).USER_ID_KEY + '_' + #userId"),
        @CacheEvict(
            value = CacheConstants.USER_CHANNEL_PREFERENCES_CACHE_VALUE,
            key = "T(com.kadioglumf.constant.CacheConstants).CHANNEL_NAME_KEY + '_' + #channel")
      })
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
        @CacheEvict(value = CacheConstants.USER_CHANNEL_PREFERENCES_CACHE_VALUE, allEntries = true)
      })
  public void refreshChannels() {}
}
