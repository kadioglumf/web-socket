package com.kadioglumf.socket.handler;

import com.kadioglumf.service.ChannelService;
import com.kadioglumf.service.NotificationService;
import com.kadioglumf.service.UserChannelPreferencesService;
import com.kadioglumf.socket.*;
import com.kadioglumf.socket.annotations.Action;
import com.kadioglumf.socket.model.enums.ActionType;
import com.kadioglumf.socket.model.enums.WsSendMessageRequest;
import com.kadioglumf.socket.utils.SubscriptionHubUtils;
import com.kadioglumf.socket.validator.ActionHandlerValidator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Log4j2
public class ActionHandler {
  private final ActionHandlerValidator actionHandlerValidator;
  private final ChannelService channelService;
  private final UserChannelPreferencesService userChannelPreferencesService;
  private final NotificationService notificationService;

  @Action(ActionType.SUBSCRIBE)
  public void subscribe(RealTimeSession session, WsSendMessageRequest request) {
    log.debug("RealTimeSession[{}] Subscribe to channel `{}`", session.id(), request.getChannel());
    actionHandlerValidator.validSubscribe(
        request.getChannel(), session.getUserDetails().getRoles());
    channelService.subscribeChannel(request.getChannel(), session.getUserDetails().getId());
    SubscriptionHubUtils.subscribe(session, request.getChannel());
  }

  @Action(ActionType.UNSUBSCRIBE)
  public void unsubscribe(RealTimeSession session, WsSendMessageRequest request) {
    log.debug(
        "RealTimeSession[{}] Unsubscribe from channel `{}`", session.id(), request.getChannel());
    actionHandlerValidator.validUnSubscribe(request.getChannel(), session);
    channelService.unsubscribeChannel(request.getChannel(), session.getUserDetails().getId());
    SubscriptionHubUtils.unsubscribe(session, request.getChannel());
  }

  @Action(ActionType.SEND_MESSAGE)
  public void send(RealTimeSession session, WsSendMessageRequest request) {
    log.debug(
        "RealTimeSession[{}] send message to channel `{}`", session.id(), request.getChannel());
    if (actionHandlerValidator.isValidSend(session, request)) {
      notificationService.save(request);
      SubscriptionHubUtils.send(request);
    }
  }

  @Action(ActionType.REFRESH_CONNECTION)
  public void refreshConnection(
      RealTimeSession session,
      WsSendMessageRequest request,
      BiConsumer<RealTimeSession, String> refreshTokenMethod) {
    log.debug("RealTimeSession[{}] refresh token", session.id());
    refreshTokenMethod.accept(session, request.getPayload().toString());
  }

  @Action(ActionType.SUBSCRIBE_ALL)
  public void subscribeAll(RealTimeSession session) {
    Set<String> channels = new HashSet<>();
    userChannelPreferencesService
        .getUserChannels(session.getUserDetails().getId())
        .forEach(
            preference -> {
              log.debug(
                  "RealTimeSession[{}] Subscribe to channel `{}`",
                  session.id(),
                  preference.getChannel());
              if (preference.isSubscribed()) {
                channelService.subscribeChannel(
                    preference.getChannel(), session.getUserDetails().getId());
                channels.add(preference.getChannel());
              }
            });
    SubscriptionHubUtils.subscribeAll(session, channels);
  }
}
