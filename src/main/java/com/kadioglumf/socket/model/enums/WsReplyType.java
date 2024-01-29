package com.kadioglumf.socket.model.enums;

public enum WsReplyType {
    SUBSCRIPTION_REPLY("subscription_reply"),
    AUTHENTICATION_SUCCESS("authentication_success"),
    AUTHENTICATION_REFRESH_SUCCESS("authentication_refresh_success")

    ;

    private final String value;

    WsReplyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
