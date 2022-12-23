package com.kadioglumf.socket;

import com.kadioglumf.socket.handler.ChannelHandlerInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SubscriptionHub {

  private static final Logger log = LoggerFactory.getLogger(SubscriptionHub.class);
  // Key is the channel, value is a set of web socket sessions that have subscribed to it
  private static final Map<String, Set<RealTimeSession>> subscriptions = new HashMap<>();
  // Keep the the channels that a client subscribed
  // The key is the session id, value is a set of subscribed channels
  private static final Map<String, Set<String>> subscribedChannels = new HashMap<>();

  public static void subscribe(RealTimeSession session, String channel) {
    Assert.hasText(channel, "Parameter `channel` must not be null");

    if (!ChannelHandlerInvoker.isRolesAllowed(session.getUserDetails().getRoles())) {
      session.error("You are not allowed to subscribe this channel!");
    }
    else {
      Set<RealTimeSession> subscribers = subscriptions.computeIfAbsent(channel, k -> new HashSet<>());
      subscribers.add(session);


      // Add the channel to client's subscribed list
      Set<String> channels = subscribedChannels.computeIfAbsent(session.id(), k -> new HashSet<>());
      channels.add(channel);
    }
  }

  public static void unsubscribe(RealTimeSession session, String channel) {
    Assert.hasText(channel, "Parameter `channel` must not be empty");
    Assert.notNull(session, "Parameter `session` must not be null");

    Set<RealTimeSession> subscribers = subscriptions.get(channel);
    if (subscribers != null) {
      subscribers.remove(session);

    }

    // Remove the channel from the client's subscribed channels
    Set<String> channels = subscribedChannels.get(session.id());
    if (channels != null) {
      channels.remove(channel);
    }
  }

  public static void unsubscribeAll(RealTimeSession session) {
    Set<String> channels = subscribedChannels.get(session.id());
    if (channels == null) {
      log.debug("RealTimeSession[{}] No channels to unsubscribe.", session.id());
      return;
    }

    for (String channel: channels) {
      unsubscribe(session, channel);
    }

    // Remove the subscribed channels
    subscribedChannels.remove(session.id());
  }

  /**
   *
   * @param request
   */
  public static void send(WsSendMessageRequest request) {
    Assert.hasText(request.getChannel(), "Parameter `channel` must not be empty");
    Assert.notNull(request.getPayload(), "Parameter `update` must not be null");

    Set<RealTimeSession> subscribers = subscriptions.get(request.getChannel());
    if (subscribers == null || subscriptions.isEmpty()) {
      log.debug("No subscribers of channel `{}` found", request.getChannel());
      return;
    }

    for (RealTimeSession subscriber: subscribers) {
      sendTo(subscriber.wrapped(), request);
    }
  }

  public static void send(RealTimeSession session, WsSendMessageRequest request) {
    Assert.hasText(request.getChannel(), "Parameter `channel` must not be empty");
    Assert.notNull(request.getPayload(), "Parameter `update` must not be null");

    if (!isSessionSubscribed(session, request.getChannel())) {
      session.error("You are not subscribed this channel!");
    }
    else {
      Set<RealTimeSession> subscribers = subscriptions.get(request.getChannel());
      if (subscribers == null || subscriptions.isEmpty()) {
        log.debug("No subscribers of channel `{}` found", request.getChannel());
        return;
      }

      for (RealTimeSession subscriber: subscribers) {
        sendTo(subscriber.wrapped(), request);
      }
    }
  }

  private static void sendTo(WebSocketSession subscriber, WsSendMessageRequest request) {
    try {
      subscriber.sendMessage(WebSocketMessages.channelMessage(request));
      log.debug("RealTimeSession[{}] Send message `{}` to subscriber at channel `{}`",
        subscriber.getId(), request.getPayload(), request.getChannel());
    } catch (IOException e) {
      log.error("Failed to send message to subscriber `" + subscriber.getId() +
        "` of channel `" + request.getChannel() + "`. Message: " + request.getPayload(), e);
    }
  }

  private static boolean isSessionSubscribed(RealTimeSession session, String channel) {
    Set<RealTimeSession> subscribers = subscriptions.get(channel);
    if (CollectionUtils.isEmpty(subscribers) || !subscribers.contains(session)) {
      return false;
    }
    return true;
  }
}
