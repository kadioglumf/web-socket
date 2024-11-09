package com.kadioglumf.socket.handler;

import com.kadioglumf.exception.WebSocketException;
import com.kadioglumf.security.TokenManager;
import com.kadioglumf.socket.RealTimeSession;
import com.kadioglumf.socket.annotations.Action;
import com.kadioglumf.socket.model.IncomingMessage;
import com.kadioglumf.socket.model.enums.ActionType;
import com.kadioglumf.socket.model.enums.WsFailureType;
import com.kadioglumf.socket.model.enums.WsSendMessageRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
@Log4j2
public class ActionInvoker {
    private final ActionHandler actionHandler;
    private final Map<ActionType, Method> actionMethods = new HashMap<>();

    public ActionInvoker(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
        bootstrap();
    }

    private void bootstrap() {
        Method[] methods = actionHandler.getClass().getMethods();
        for (Method method : methods) {
            Action actionAnnotation = method.getAnnotation(Action.class);
            if (actionAnnotation == null) {
                continue;
            }

            ActionType action = actionAnnotation.value();
            actionMethods.put(action, method);
            //log.debug("Mapped action `{}` in channel handler `{}#{}`", action, handlerClass.getName(), method);
        }
    }

    public void invokeAction(IncomingMessage incomingMessage, RealTimeSession session, BiConsumer<RealTimeSession, String> func) {
        try {
            if (!supports(incomingMessage.getAction())) {
                String errorMessage = "Action not found `" + incomingMessage.getAction() +
                        "` at channel `" + incomingMessage.getChannel() + "`";
                session.fail(WsFailureType.ACTION_TYPE_FAILURE.getValue());
                log.error("RealTimeSession[{}] {}", session.id(), errorMessage);
                return;
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
                } else if (parameterType.isAssignableFrom(WsSendMessageRequest.class)) {
                    var sendMessageRequest = WsSendMessageRequest.builder()
                            .infoType(incomingMessage.getInfoType())
                            .category(incomingMessage.getCategory())
                            .payload(incomingMessage.getPayload())
                            .channel(incomingMessage.getChannel())
                            .sendingType(incomingMessage.getSendingType())
                            .role(incomingMessage.getRole())
                            .userId(incomingMessage.getUserId())
                            .build();

                    args[i] = sendMessageRequest;
                }
                else if (parameterType.isAssignableFrom(BiConsumer.class)) {
                    args[i] = func;
                }
            }

            actionMethod.invoke(actionHandler, args);
        } catch (Exception e) {
            if (e instanceof InvocationTargetException && ((InvocationTargetException) e).getTargetException() instanceof WebSocketException) {
                WebSocketException exception = (WebSocketException) ((InvocationTargetException) e).getTargetException();
                String error = exception.getErrorResponse().getErrorMessage();
                log.error(error, e);
                session.fail(error);
                return;
            }
            String error = "Failed to invoker action method `" + incomingMessage.getAction() +
                    "` at channel `" + incomingMessage.getChannel() + "` ";
            log.error(error, e);
            session.fail(WsFailureType.UNKNOWN_FAILURE.getValue());
        }
    }

    public boolean supports(String action) {
        return actionMethods.containsKey(ActionType.getFromValue(action));
    }
}
