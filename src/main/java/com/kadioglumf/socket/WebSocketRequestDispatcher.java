package com.kadioglumf.socket;

import com.kadioglumf.exception.ErrorType;
import com.kadioglumf.exception.WebSocketException;
import com.kadioglumf.model.UserDetailsImpl;
import com.kadioglumf.security.TokenManager;
import com.kadioglumf.socket.handler.ActionInvoker;
import com.kadioglumf.socket.model.IncomingMessage;
import com.kadioglumf.socket.model.enums.ActionType;
import com.kadioglumf.socket.model.enums.WsFailureType;
import com.kadioglumf.socket.model.enums.WsReplyType;
import com.kadioglumf.socket.utils.SubscriptionHubUtils;
import com.kadioglumf.utils.ConvertUtils;
import java.io.IOException;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Log4j2
public class WebSocketRequestDispatcher extends TextWebSocketHandler {

  private final ActionInvoker actionInvoker;
  private final TokenManager tokenManager;

  private final Map<String, RealTimeSession> allSessions = new HashMap<>();

  public WebSocketRequestDispatcher(ActionInvoker actionInvoker, TokenManager tokenManager) {
    this.actionInvoker = actionInvoker;
    this.tokenManager = tokenManager;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession webSocketSession) throws IOException {
    log.debug("WebSocket connection established");
    RealTimeSession session = new RealTimeSession(webSocketSession);
    try {
      UserDetailsImpl userDetails = tokenManager.getUserDetailsByJwt(session.getTokenFromUrl());
      if (userDetails == null) {
        throw new WebSocketException(ErrorType.WEB_SOCKET_ERROR, "Authentication failed!");
      }

      session.setUserDetails(userDetails);
      session.setLastValidToken(session.getTokenFromUrl());
      session.reply(WsReplyType.AUTHENTICATION_SUCCESS.getValue(), null);
      allSessions.put(session.id(), session);

      IncomingMessage incomingMessage = new IncomingMessage();
      incomingMessage.setAction(ActionType.SUBSCRIBE_ALL.getValue());
      handleTextMessage(
          webSocketSession, new TextMessage(ConvertUtils.toJsonData(incomingMessage)));
    } catch (WebSocketException exception) {
      log.debug("Authentication failed");
      session.fail(WsFailureType.AUTHENTICATION_FAILURE.getValue());
      webSocketSession.close(CloseStatus.SERVER_ERROR);
    } catch (Exception exception) {
      log.debug("Error afterConnectionEstablished method: {}", exception.getMessage());
      session.fail(WsFailureType.AUTHENTICATION_FAILURE.getValue());
      webSocketSession.close(CloseStatus.SERVER_ERROR);
    }
  }

  @Override
  protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message)
      throws IOException {
    RealTimeSession session = allSessions.get(webSocketSession.getId());
    log.debug("RealTimeSession[{}] Received message `{}`", session.id(), message.getPayload());

    IncomingMessage incomingMessage =
        ConvertUtils.toObject(message.getPayload(), IncomingMessage.class);
    if (incomingMessage == null) {
      session.fail(WsFailureType.ILLEGAL_MESSAGE_FORMAT_FAILURE.getValue());
      return;
    }

    actionInvoker.invokeAction(incomingMessage, session, this::refreshConnection);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status) {
    try {
      RealTimeSession session = allSessions.get(webSocketSession.getId());
      if (session != null) {
        SubscriptionHubUtils.unsubscribeAll(session);
        allSessions.remove(session.id());
        session.wrapped().close(status);
        log.debug(
            "RealTimeSession[{}] Unsubscribed all channels after disconnecting", session.id());
      }
    } catch (IOException e) {
      log.error("Error afterConnectionClosed method: {}", e.getMessage());
    }
  }

  private void refreshConnection(RealTimeSession session, String token) {
    try {
      UserDetailsImpl userDetails = tokenManager.getUserDetailsByJwt(token);
      if (userDetails == null) {
        throw new WebSocketException(ErrorType.WEB_SOCKET_ERROR, "Authentication failed!");
      }
      session.setLastValidToken(token);
      session.reply(WsReplyType.AUTHENTICATION_REFRESH_SUCCESS.getValue(), null);
    } catch (WebSocketException exception) {
      session.fail(WsFailureType.AUTHENTICATION_FAILURE.getValue());
      afterConnectionClosed(session.wrapped(), CloseStatus.SERVER_ERROR);
    } catch (Exception exception) {
      log.debug("Error handleTextMessage method: {}", exception.getMessage());
      session.fail(WsFailureType.UNKNOWN_FAILURE.getValue());
      afterConnectionClosed(session.wrapped(), CloseStatus.SERVER_ERROR);
    }
  }

  public void checkTokenOfAllSessions() {
    for (var session : allSessions.entrySet()) {
      if (session.getValue().isSubscriberTokenExpired()) {
        session.getValue().fail(WsFailureType.AUTH_TOKEN_EXPIRED_FAILURE.getValue());
        afterConnectionClosed(session.getValue().wrapped(), CloseStatus.SERVER_ERROR);
      }
    }
  }
}
