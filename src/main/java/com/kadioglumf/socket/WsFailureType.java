package com.kadioglumf.socket;

public enum WsFailureType {
    SEND_FAILURE("send_failure", "You are not subscribed this channel!"),
    AUTHENTICATION_FAILURE("auth_failure", "Authentication failure!"),
    ILLEGAL_MESSAGE_FORMAT_FAILURE("illegal_format_failure", "Illegal message format failure!"),
    ACTION_TYPE_FAILURE("action_type_failure", "No handler found for action failure!"),
    AUTH_TOKEN_EXPIRED_FAILURE("auth_token_expired_failure", "Token expired failure!"),
    MISSING_FIELD_FAILURE("missing_field_failure", "Missing filed failure!"),
    UNKNOWN_FAILURE("unknown_failure", "Unknown failure!");

    private final String value;
    private final String description;

    WsFailureType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
