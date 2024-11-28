package com.kadioglumf.service;

import com.kadioglumf.dto.response.UserPreferencesResponseDto;
import com.kadioglumf.model.UserChannelPreferences;
import com.kadioglumf.repository.ChannelRepository;
import com.kadioglumf.repository.UserChannelPreferencesRepository;
import com.kadioglumf.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserChannelPreferencesService {
  private final UserChannelPreferencesRepository userChannelPreferencesRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Transactional
  public List<UserPreferencesResponseDto> getUserChannels(Long userId) {
    var preferences = userChannelPreferencesRepository.findAllByUserId(userId);
    if (CollectionUtils.isEmpty(preferences)) {
      var user = userRepository.findById(userId);
      var channels = channelRepository.findAllByRoles(user.get().getRoles());
      channels.forEach(
          c -> {
            var preference =
                UserChannelPreferences.builder()
                    .channel(c)
                    .userId(userId)
                    .isSubscribed(true)
                    .build();
            userChannelPreferencesRepository.save(preference);

            c.getUserChannelPreferences().add(preference);
            channelRepository.save(c);
          });

      return channels.stream()
          .map(
              c ->
                  UserPreferencesResponseDto.builder()
                      .channel(c.getName())
                      .isSubscribed(true)
                      .userId(userId)
                      .build())
          .collect(Collectors.toList());
    }
    return preferences.stream()
        .map(
            c ->
                UserPreferencesResponseDto.builder()
                    .channel(c.getChannel().getName())
                    .isSubscribed(c.isSubscribed())
                    .userId(userId)
                    .build())
        .collect(Collectors.toList());
  }

  public List<UserPreferencesResponseDto> getByChannelName(String channelName) {
    var preferences = userChannelPreferencesRepository.findAllByChannel_Name(channelName);
    return preferences.stream()
        .map(
            c ->
                UserPreferencesResponseDto.builder()
                    .channel(c.getChannel().getName())
                    .isSubscribed(c.isSubscribed())
                    .userId(c.getUserId())
                    .build())
        .collect(Collectors.toList());
  }
}
