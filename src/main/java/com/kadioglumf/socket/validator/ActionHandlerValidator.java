package com.kadioglumf.socket.validator;

import com.kadioglumf.exception.ErrorType;
import com.kadioglumf.exception.WebSocketException;
import com.kadioglumf.socket.RealTimeSession;
import com.kadioglumf.socket.model.enums.WsFailureType;
import com.kadioglumf.socket.model.enums.WsSendMessageRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Log4j2
public class ActionHandlerValidator {
  private final ChannelValidator channelValidator;

  public void validSubscribe(String channel, List<String> userRoles) {
    channelValidator.valid(channel, userRoles);
  }

  public void validUnSubscribe(String channel, RealTimeSession session) {
    if (StringUtils.isBlank(channel)) {
      throw new WebSocketException(ErrorType.WEB_SOCKET_ERROR, "Channel cannot be blank!");
    }
    if (session == null) {
      throw new WebSocketException(ErrorType.WEB_SOCKET_ERROR, "Session cannot be null!");
    }
    if (!session.wrapped().isOpen()) {
      log.error(
          "{} session closed. email: {} should reconnect.",
          session.id(),
          session.getUserDetails().getEmail());
      throw new WebSocketException(ErrorType.WEB_SOCKET_ERROR, "Session is not open!");
    }
  }

  public boolean isValidSend(RealTimeSession session, WsSendMessageRequest request) {
    channelValidator.valid(request.getChannel(), session.getUserDetails().getRoles());
    if (request.getCategory() == null || request.getInfoType() == null) {
      session.fail(WsFailureType.MISSING_FIELD_FAILURE.getValue());
      return false;
    }
    if (!session.isAdmin()) {
      session.fail(WsFailureType.SEND_FAILURE.getValue());
      return false;
    }
    return true;
  }
}
