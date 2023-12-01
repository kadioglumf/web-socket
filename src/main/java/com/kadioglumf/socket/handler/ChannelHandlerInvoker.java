package com.kadioglumf.socket.handler;

import com.kadioglumf.exception.ErrorType;
import com.kadioglumf.exception.WebSocketException;
import com.kadioglumf.socket.*;
import com.kadioglumf.socket.annotations.Action;
import com.kadioglumf.socket.annotations.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelHandlerInvoker {

  private static final Logger log = LoggerFactory.getLogger(ChannelHandlerInvoker.class);

  private static final AntPathMatcher antPathMatcher = new AntPathMatcher();


  private String value;
  private List<String> allowedRoles;

  private Object handler;
  // Key is the action, value is the method to handle that action
  private final Map<ActionType, Method> actionMethods = new HashMap<>();

  public ChannelHandlerInvoker(Object handler) {
    Assert.notNull(handler, "Parameter `handler` must not be null");

    Class<?> handlerClass = handler.getClass();
    ChannelHandler handlerAnnotation = handlerClass.getAnnotation(ChannelHandler.class);
    Assert.notNull(handlerAnnotation, "Parameter `handler` must have annotation @ChannelHandler");

    Method[] methods = handlerClass.getMethods();
    for (Method method : methods) {
      Action actionAnnotation = method.getAnnotation(Action.class);
      if (actionAnnotation == null) {
        continue;
      }

      ActionType action = actionAnnotation.value();
      actionMethods.put(action, method);
      log.debug("Mapped action `{}` in channel handler `{}#{}`", action, handlerClass.getName(), method);
    }

    this.value = handlerAnnotation.value();
    this.allowedRoles = Arrays.asList(handlerAnnotation.allowedRoles());
    this.handler = handler;
  }

  public boolean supports(String action) {
    return actionMethods.containsKey(ActionType.getFromValue(action));
  }

  public boolean isRolesAllowed(List<String> roles) {
    if (CollectionUtils.isEmpty(allowedRoles)) {
      return true;
    }
    for (String role : roles) {
      for (String allowedRole : allowedRoles) {
        if (role.equals(allowedRole)) {
          return true;
        }
      }
    }
    return false;
  }

  public void handle(IncomingMessage incomingMessage, RealTimeSession session) {
    try {
      Assert.isTrue(antPathMatcher.match(value, incomingMessage.getChannel()), "Channel of the handler must match");

      if (!isRolesAllowed(session.getUserDetails().getRoles())) {
        throw new WebSocketException(ErrorType.WEB_SOCKET_ERROR, "You are not allowed to subscribe this channel!");
      }

      Method actionMethod = actionMethods.get(ActionType.getFromValue(incomingMessage.getAction()));
      Assert.notNull(actionMethod, "Action method for `" + incomingMessage.getAction() + "` must exist");

      // Find all required parameters
      Class<?>[] parameterTypes = actionMethod.getParameterTypes();

      // The arguments that will be passed to the action method
      Object[] args = new Object[parameterTypes.length];

      for (int i = 0; i < parameterTypes.length; i++) {
        Class<?> parameterType = parameterTypes[i];

        if (parameterType.isInstance(session)) {
          args[i] = session;
        }
        else if (parameterType.isAssignableFrom(WsSendMessageRequest.class)) {
          WsSendMessageRequest sendMessageRequest = new WsSendMessageRequest();
          sendMessageRequest.setInfoType(incomingMessage.getInfoType());
          sendMessageRequest.setCategory(incomingMessage.getCategory());
          sendMessageRequest.setPayload(incomingMessage.getPayload());
          sendMessageRequest.setChannel(incomingMessage.getChannel());
          sendMessageRequest.setSendingType(incomingMessage.getSendingType());
          sendMessageRequest.setRole(incomingMessage.getRole());
          sendMessageRequest.setUserId(incomingMessage.getUserId());

          args[i] = sendMessageRequest;
        }
      }

      actionMethod.invoke(handler, args);
    }  catch (WebSocketException e) {
      String error = e.getErrorResponse().getErrorMessage();
      log.error(error, e);
      session.fail(error);
    } catch (Exception e) {
      String error = "Failed to invoker action method `" + incomingMessage.getAction() +
              "` at channel `" + incomingMessage.getChannel() + "` ";
      log.error(error, e);
      session.fail(WsFailureType.UNKNOWN_FAILURE.getValue());
    }
  }
}
