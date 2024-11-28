package com.kadioglumf.service;

import com.kadioglumf.constant.CacheConstants;
import com.kadioglumf.dto.response.NotificationResponseDto;
import com.kadioglumf.dto.response.UserPreferencesResponseDto;
import com.kadioglumf.mapper.NotificationMapper;
import com.kadioglumf.model.Notification;
import com.kadioglumf.repository.NotificationRepository;
import com.kadioglumf.repository.UserRepository;
import com.kadioglumf.socket.model.enums.WsSendMessageRequest;
import com.kadioglumf.socket.model.enums.WsSendingType;
import com.kadioglumf.utils.UserThreadContext;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {
  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final CacheManager cacheManager;
  private final UserChannelPreferencesService userChannelPreferencesService;

  @Transactional
  public void save(WsSendMessageRequest request) {
    if (WsSendingType.SPECIFIED_USER.equals(request.getSendingType())) {
      var preferences = userChannelPreferencesService.getByChannelName(request.getChannel());
      var usersIds =
          preferences.stream()
              .filter(p -> p.getUserId().equals(request.getUserId()))
              .filter(UserPreferencesResponseDto::isSubscribed)
              .map(UserPreferencesResponseDto::getUserId)
              .collect(Collectors.toSet());
      save(request, usersIds);
    } else if (WsSendingType.ROLE_BASED.equals(request.getSendingType())) {
      var preferences = userChannelPreferencesService.getByChannelName(request.getChannel());

      var users = userRepository.findAllByRoles(Set.of(request.getRole().name()));
      var usersIds =
          preferences.stream()
              .filter(UserPreferencesResponseDto::isSubscribed)
              .map(UserPreferencesResponseDto::getUserId)
              .filter(userId -> users.stream().anyMatch(u -> u.getId().equals(userId)))
              .collect(Collectors.toSet());
      save(request, usersIds);
    } else if (request.getSendingType() == null
        || WsSendingType.ALL.equals(request.getSendingType())) {
      var preferences = userChannelPreferencesService.getByChannelName(request.getChannel());

      var usersIds =
          preferences.stream()
              .filter(UserPreferencesResponseDto::isSubscribed)
              .map(UserPreferencesResponseDto::getUserId)
              .collect(Collectors.toSet());
      save(request, usersIds);
    }
  }

  public void save(WsSendMessageRequest request, Set<Long> userIds) {
    for (var userId : userIds) {
      var model = new Notification();
      model.setNotificationId(UUID.randomUUID().toString());
      model.setMessage(request.getPayload().toString());
      model.setInfoType(request.getInfoType());
      model.setCategory(request.getCategory());
      model.setChannel(request.getChannel());
      model.setUserId(userId);
      notificationRepository.save(model);

      var cache = cacheManager.getCache(CacheConstants.NOTIFICATION_CACHE_VALUE);
      if (cache != null) {
        cache.evictIfPresent("userId_" + userId);
      }
    }
  }

  @Cacheable(
      value = CacheConstants.NOTIFICATION_CACHE_VALUE,
      key = "'userId_' + T(com.kadioglumf.utils.UserThreadContext).getUserId()")
  public List<NotificationResponseDto> fetch() {
    var notifications = notificationRepository.findByUserId(UserThreadContext.getUserId());
    return notificationMapper.toNotificationResponseList(notifications);
  }

  @CacheEvict(
      value = CacheConstants.NOTIFICATION_CACHE_VALUE,
      key = "'userId_' + T(com.kadioglumf.utils.UserThreadContext).getUserId()")
  @Transactional
  public void markAsRead(List<String> notificationId) {
    var notification = notificationRepository.findByNotificationIdIn(notificationId);
    if (notification == null) {
      return;
    }
    notification.forEach(n -> n.setRead(true));
    notificationRepository.saveAll(notification);
  }

  @CacheEvict(
      value = CacheConstants.NOTIFICATION_CACHE_VALUE,
      key = "'userId_' + T(com.kadioglumf.utils.UserThreadContext).getUserId()")
  @Transactional
  public void deleteAll() {
    var notifications = notificationRepository.findByUserId(UserThreadContext.getUserId());
    notifications.forEach(notification -> notification.setDeleted(true));
    notificationRepository.saveAll(notifications);
  }
}
