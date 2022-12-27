package com.kadioglumf.socket;

import com.kadioglumf.exception.ErrorType;
import com.kadioglumf.exception.WebSocketException;
import com.kadioglumf.model.UserDetails;
import com.kadioglumf.security.TokenManager;
import com.kadioglumf.socket.handler.ChannelHandlerInvoker;
import com.kadioglumf.socket.handler.ChannelHandlerResolver;
import com.kadioglumf.utils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Component
public class WebSocketRequestDispatcher extends TextWebSocketHandler {

  private static final Logger log = LoggerFactory.getLogger(WebSocketRequestDispatcher.class);


  private final ChannelHandlerResolver channelHandlerResolver;
  private final TokenManager tokenManager;

  private final Map<String, RealTimeSession> allSessions = new HashMap<>();

  public WebSocketRequestDispatcher(ChannelHandlerResolver channelHandlerResolver, TokenManager tokenManager) {
    this.channelHandlerResolver = channelHandlerResolver;
    this.tokenManager = tokenManager;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession webSocketSession) throws IOException {
    log.debug("WebSocket connection established");
    RealTimeSession session = new RealTimeSession(webSocketSession);

    try {
      if (session.getUserDetails() == null) {
        throw new WebSocketException(ErrorType.WEB_SOCKET_ERROR, "Authentication failed!");
      }
      session.setLastValidToken(session.getTokenFromHeader());
      session.reply("authenticated");
      allSessions.put(session.id(), session);
    } catch (WebSocketException exception) {
      log.debug("Authentication failed");
      session.fail(exception.getErrorResponse().getErrorMessage());
      webSocketSession.close(CloseStatus.SERVER_ERROR);
    } catch (Exception exception) {
      log.debug("Error afterConnectionEstablished method: {}", exception.getMessage());
      session.fail("authentication failed" + exception.getMessage());
      webSocketSession.close(CloseStatus.SERVER_ERROR);
    }
  }

  @Override
  protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) throws IOException {
    RealTimeSession session = allSessions.get(webSocketSession.getId());
    log.debug("RealTimeSession[{}] Received message `{}`", session.id(), message.getPayload());

    IncomingMessage incomingMessage = ConvertUtils.ToObject(message.getPayload(), IncomingMessage.class);
    if (incomingMessage == null) {
      session.error("Illegal format of incoming message: " + message.getPayload());
      return;
    }

    if (WsEventType.REFRESH_CONNECTION.equals(incomingMessage.getEvent())) {

      try {
        UserDetails userDetails = tokenManager.getUserDetailsByJwt(incomingMessage.getPayload().toString());
        if (userDetails == null) {
          throw new WebSocketException(ErrorType.WEB_SOCKET_ERROR, "Authentication failed!");
        }
        session.setLastValidToken(incomingMessage.getPayload().toString());
      } catch (WebSocketException exception) {
        session.fail(exception.getErrorResponse().getErrorMessage());
        afterConnectionClosed(webSocketSession, CloseStatus.SERVER_ERROR);
      } catch (Exception exception) {
        log.debug("Error handleTextMessage method: {}", exception.getMessage());
        session.fail("handleTextMessage failed" + exception.getMessage());
        afterConnectionClosed(webSocketSession, CloseStatus.SERVER_ERROR);
      }

    }
    else {
      ChannelHandlerInvoker invoker = channelHandlerResolver.findInvoker(incomingMessage);
      if (invoker == null) {
        String errorMessage = "No handler found for action `" + incomingMessage.getAction() +
                "` at channel `" + incomingMessage.getChannel() + "`";
        session.error(errorMessage);
        log.error("RealTimeSession[{}] {}", session.id(), errorMessage);
        return;
      }

      invoker.handle(incomingMessage, session);
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status) throws IOException {
    RealTimeSession session = allSessions.get(webSocketSession.getId());
    if (session != null) {
      SubscriptionHub.unsubscribeAll(session);
      allSessions.remove(session.id());
      session.wrapped().close(status);
      log.debug("RealTimeSession[{}] Unsubscribed all channels after disconnecting", session.id());
    }
  }

/*  @Scheduled(fixedRateString = "60000")
  public void checkTokenOfAllSessions() {
    for (var session : allSessions.entrySet()) {
      try {
        if (session.getValue().isSubscriberTokenExpired()) {
          session.getValue().fail("Token is expired!");
          afterConnectionClosed(session.getValue().wrapped(), CloseStatus.SERVER_ERROR);
        }
      }
      catch (IOException ex) {
        log.error("");
      }
    }
  }*/
}
