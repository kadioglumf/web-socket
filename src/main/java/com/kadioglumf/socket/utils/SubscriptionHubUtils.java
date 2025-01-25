package com.kadioglumf.socket.utils;

import com.kadioglumf.socket.RealTimeSession;
import com.kadioglumf.socket.model.enums.WsReplyType;
import com.kadioglumf.socket.model.enums.WsSendMessageRequest;
import com.kadioglumf.socket.model.enums.WsSendingType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SubscriptionHubUtils {

  private static final Logger log = LoggerFactory.getLogger(SubscriptionHubUtils.class);
  // Key is the channel, value is a set of web socket sessions that have subscribed to it
  @Getter private static final Map<String, Set<RealTimeSession>> subscriptions = new HashMap<>();
  // Keep the the channels that a client subscribed
  // The key is the session id, value is a set of subscribed channels
  @Getter private static final Map<String, Set<String>> subscribedChannels = new HashMap<>();

  /** Method that performs the process of subscribing to the channel. */
  public static void subscribe(RealTimeSession session, String channel) {
    Set<RealTimeSession> subscribers = subscriptions.computeIfAbsent(channel, k -> new HashSet<>());
    subscribers.add(session);

    // Add the channel to client's subscribed list
    Set<String> channels = subscribedChannels.computeIfAbsent(session.id(), k -> new HashSet<>());
    channels.add(channel);
    session.reply(WsReplyType.SUBSCRIPTION_REPLY.getValue(), channels);
  }

  /** Method that performs the process of subscribing to the channel. */
  public static void subscribeAll(RealTimeSession session, Set<String> channelList) {
    channelList.forEach(
        c -> {
          Set<RealTimeSession> subscribers = subscriptions.computeIfAbsent(c, k -> new HashSet<>());
          subscribers.add(session);

          // Add the channel to client's subscribed list
          Set<String> channels =
              subscribedChannels.computeIfAbsent(session.id(), k -> new HashSet<>());
          channels.add(c);
        });
    session.reply(WsReplyType.SUBSCRIPTION_REPLY.getValue(), subscribedChannels.get(session.id()));
  }

  /** Method that performs the process of unsubscribing from the channel. */
  public static void unsubscribe(RealTimeSession session, String channel) {
    Set<RealTimeSession> subscribers = subscriptions.get(channel);
    if (subscribers != null) {
      subscribers.remove(session);
    }

    // Remove the channel from the client's subscribed channels
    Set<String> channels = subscribedChannels.get(session.id());
    if (channels != null) {
      channels.remove(channel);
    }
    session.reply(WsReplyType.SUBSCRIPTION_REPLY.getValue(), channels);
  }

  /** Method that performs the process of unsubscribing from the all channels. */
  public static void unsubscribeAll(RealTimeSession session) {
    if (subscribedChannels.get(session.id()) == null) {
      log.debug("RealTimeSession[{}] No channels to unsubscribe.", session.id());
      return;
    }
    Set<String> channels = new HashSet<>(subscribedChannels.get(session.id()));

    for (String channel : channels) {
      unsubscribe(session, channel);
    }

    // Remove the subscribed channels
    subscribedChannels.remove(session.id());
  }

  /**
   * This is the method used to broadcast the message. This method can be used when the backend will
   * publish a message. Because the backend does not have to subscribe.
   */
  public static void send(WsSendMessageRequest request) {
    Set<RealTimeSession> subscribers = subscriptions.get(request.getChannel());
    if (subscribers == null || subscriptions.isEmpty()) {
      log.debug("No subscribers of channel `{}` found", request.getChannel());
      return;
    }

    for (RealTimeSession subscriber : subscribers) {
      sendTo(subscriber, request);
    }
  }

  /** It is the method in which the @param subscriber in the request broadcasts the message. */
  private static void sendTo(RealTimeSession subscriber, WsSendMessageRequest request) {

    if (!subscriber.wrapped().isOpen()) {
      unsubscribeAll(subscriber);
      return;
    }
    if (WsSendingType.ROLE_BASED.equals(request.getSendingType())) {

      boolean isRoleFound =
          request.getRole() != null
              && subscriber.getUserDetails().getRoles().contains(request.getRole().name());

      if (isRoleFound) {
        subscriber.info(request);
        log.debug(
            "RealTimeSession[{}] Send message `{}` to subscriber at channel `{}`",
            subscriber.id(),
            request.getPayload(),
            request.getChannel());
      }
    } else if (WsSendingType.SPECIFIED_USER.equals(request.getSendingType())) {

      boolean isSpecifiedUserFound =
          request.getUserId() != null
              && request.getUserId().equals(subscriber.getUserDetails().getId());

      if (isSpecifiedUserFound) {
        subscriber.info(request);
        log.debug(
            "RealTimeSession[{}] Send message `{}` to subscriber at channel `{}`",
            subscriber.id(),
            request.getPayload(),
            request.getChannel());
      }
    } else {
      subscriber.info(request);
      log.debug(
          "RealTimeSession[{}] Send message `{}` to subscriber at channel `{}`",
          subscriber.id(),
          request.getPayload(),
          request.getChannel());
    }
  }
}
