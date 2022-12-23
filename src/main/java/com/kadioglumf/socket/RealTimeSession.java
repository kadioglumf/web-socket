package com.kadioglumf.socket;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kadioglumf.exception.ErrorType;
import com.kadioglumf.exception.WebSocketException;
import com.kadioglumf.model.UserDetails;
import com.kadioglumf.security.TokenManager;
import com.kadioglumf.utils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * A wrapper over {@link WebSocketSession} to add convenient methods
 */
public class RealTimeSession {

  private static final Logger log = LoggerFactory.getLogger(RealTimeSession.class);
  private static final String LAST_VALID_TOKEN = "LAST_VALID_TOKEN";

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

  public UserDetails getUserDetails() {
    UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) this.session.getPrincipal();
    return authenticationToken == null ? null : (UserDetails) authenticationToken.getPrincipal();
  }

  public String getTokenFromHeader() {
    List<String> headerAuths = session.getHandshakeHeaders().get(TokenManager.AUTHORIZATION_HEADER);
    if (CollectionUtils.isEmpty(headerAuths)) {
      throw new WebSocketException(ErrorType.WEB_SOCKET_ERROR); //TODO
    }
    return headerAuths.get(0);
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
      if(jwt.getExpiresAt().before(new Date())) {
        return true;
      }
    }
    catch (Exception ex) {
      return true;
    }
    return false;
  }

  public void error(String error) {
    sendMessage(WebSocketMessages.error(error));
  }

  public void fail(String failure) {
    sendMessage(WebSocketMessages.failure(failure));
  }

  public void reply(String reply) {
    sendMessage(WebSocketMessages.reply(reply));
  }

  private void sendMessage(Object message) {
    try {
      String textMessage = ConvertUtils.ToJsonData(message);
      session.sendMessage(new TextMessage(textMessage));
    } catch (IOException e) {
      log.error("Failed to send message through web socket session", e);
    }
  }
}
