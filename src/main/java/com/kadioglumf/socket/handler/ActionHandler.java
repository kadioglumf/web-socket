package com.kadioglumf.socket.handler;

import com.kadioglumf.exception.ErrorType;
import com.kadioglumf.exception.WebSocketException;
import com.kadioglumf.model.UserDetailsImpl;
import com.kadioglumf.security.TokenManager;
import com.kadioglumf.socket.model.enums.ActionType;
import com.kadioglumf.socket.model.enums.WsFailureType;
import com.kadioglumf.socket.model.enums.WsReplyType;
import com.kadioglumf.socket.model.enums.WsSendMessageRequest;
import com.kadioglumf.socket.*;
import com.kadioglumf.socket.annotations.Action;
import com.kadioglumf.socket.validator.ChannelValidator;
import com.kadioglumf.socket.utils.SubscriptionHubUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.BiConsumer;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Log4j2
public class ActionHandler {
    private final ChannelValidator channelValidator;

    @Action(ActionType.SUBSCRIBE)
    public void subscribe(RealTimeSession session, WsSendMessageRequest request) {
        log.debug("RealTimeSession[{}] Subscribe to channel `{}`", session.id(), request.getChannel());
        channelValidator.valid(request.getChannel(), session.getUserDetails().getRoles());
        SubscriptionHubUtils.subscribe(session, request.getChannel());
    }

    @Action(ActionType.UNSUBSCRIBE)
    public void unsubscribe(RealTimeSession session, WsSendMessageRequest request) {
        log.debug("RealTimeSession[{}] Unsubscribe from channel `{}`", session.id(), request.getChannel());
        channelValidator.valid(request.getChannel(), session.getUserDetails().getRoles());
        SubscriptionHubUtils.unsubscribe(session, request.getChannel());
    }

    @Action(ActionType.SEND_MESSAGE)
    public void send(RealTimeSession session, WsSendMessageRequest request) {
        log.debug("RealTimeSession[{}] send message to channel `{}`", session.id(), request.getChannel());
        channelValidator.valid(request.getChannel(), session.getUserDetails().getRoles());
        if (request.getCategory() == null || request.getInfoType() == null) {
            session.fail(WsFailureType.MISSING_FIELD_FAILURE.getValue());
            return;
        }
        SubscriptionHubUtils.send(session, request);
    }

    @Action(ActionType.REFRESH_CONNECTION)
    public void refreshConnection(RealTimeSession session, WsSendMessageRequest request, BiConsumer<RealTimeSession, String> refreshTokenMethod) {
        log.debug("RealTimeSession[{}] refresh token", session.id());
        refreshTokenMethod.accept(session, request.getPayload().toString());
    }

}
