package com.kadioglumf.socket;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kadioglumf.model.UserDetailsImpl;
import com.kadioglumf.security.TokenManager;
import com.kadioglumf.socket.model.enums.WsSendMessageRequest;
import com.kadioglumf.socket.utils.WebSocketMessagesUtils;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/** A wrapper over {@link WebSocketSession} to add convenient methods */
public class RealTimeSession {

  private static final Logger log = LoggerFactory.getLogger(RealTimeSession.class);
  private static final String LAST_VALID_TOKEN = "LAST_VALID_TOKEN";
  private static final String USER_DETAILS = "USER_DETAILS";

  private final WebSocketSession session;

  RealTimeSession(WebSocketSession session) {
    this.session = session;
  }

  public String id() {
    return session.getId();
  }

  public WebSocketSession wrapped() {
    return session;
  }

  public UserDetailsImpl getUserDetails() {
    return getAttribute(USER_DETAILS);
  }

  public void setUserDetails(UserDetailsImpl userDetails) {
    addAttribute(USER_DETAILS, userDetails);
  }

  public String getTokenFromUrl() {
    URI uri = session.getUri();
    UriComponents uriComponents = UriComponentsBuilder.fromUri(uri).build();
    return uriComponents.getQueryParams().getFirst(TokenManager.AUTHORIZATION_HEADER);
  }

  public String getLastValidToken() {
    return getAttribute(LAST_VALID_TOKEN);
  }

  public void setLastValidToken(String token) {
    addAttribute(LAST_VALID_TOKEN, token);
  }

  void addAttribute(String key, Object value) {
    session.getAttributes().put(key, value);
  }

  @SuppressWarnings("unchecked")
  public <T> T getAttribute(String key) {
    Object value = session.getAttributes().get(key);
    if (value == null) {
      return null;
    }
    return (T) value;
  }

  public boolean isSubscriberTokenExpired() {
    try {
      DecodedJWT jwt = JWT.decode(getLastValidToken());
      if (jwt.getExpiresAt().before(new Date())) {
        return true;
      }
    } catch (Exception ex) {
      return true;
    }
    return false;
  }

  public void info(WsSendMessageRequest request) {
    sendMessage(WebSocketMessagesUtils.info(request));
  }

  public void fail(String failure) {
    sendMessage(WebSocketMessagesUtils.failure(failure));
  }

  public void reply(String reply, @Nullable Set<String> subscribedChannels) {
    sendMessage(WebSocketMessagesUtils.reply(reply, subscribedChannels));
  }

  private void sendMessage(TextMessage message) {
    try {
      session.sendMessage(message);
    } catch (IOException e) {
      log.error("Failed to send message through web socket session", e);
    }
  }

  public boolean isAdmin() {
    return getUserDetails().getRoles().contains("ROLE_ADMIN");
  }
  /*  public String getTokenFromHeader() {
    List<String> headerAuths = session.getHandshakeHeaders().get(TokenManager.AUTHORIZATION_HEADER);
    if (CollectionUtils.isEmpty(headerAuths)) {
      throw new WebSocketException(ErrorType.WEB_SOCKET_ERROR); //TODO
    }
    return headerAuths.get(0);
  }*/
}
