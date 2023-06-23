package com.kadioglumf.socket.handler;

import com.kadioglumf.socket.*;
import com.kadioglumf.socket.annotations.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseChannelHandler {

    private static final Logger log = LoggerFactory.getLogger(BaseChannelHandler.class);

    @Action(ActionType.SUBSCRIBE)
    public void subscribe(RealTimeSession session, WsSendMessageRequest request) {
        log.debug("RealTimeSession[{}] Subscribe to channel `{}`", session.id(), request.getChannel());
        SubscriptionHub.subscribe(session, request.getChannel());
    }

    @Action(ActionType.UNSUBSCRIBE)
    public void unsubscribe(RealTimeSession session, WsSendMessageRequest request) {
        log.debug("RealTimeSession[{}] Unsubscribe from channel `{}`", session.id(), request.getChannel());
        SubscriptionHub.unsubscribe(session, request.getChannel());
    }

    @Action(ActionType.SEND_MESSAGE)
    public void send(RealTimeSession session, WsSendMessageRequest request) {
        log.debug("RealTimeSession[{}] send message to channel `{}`", session.id(), request.getChannel());
        if (request.getCategory() == null || request.getInfoType() == null) {
            session.fail(WsFailureType.MISSING_FIELD_FAILURE.getValue());
            return;
        }
        SubscriptionHub.send(session, request);
    }
}
