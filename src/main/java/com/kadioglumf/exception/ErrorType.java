package com.kadioglumf.exception;

public enum ErrorType {

    UNKNOWN_ERROR(1001, "Unknown error", 500),
    WEB_SOCKET_ERROR(1002, "Websocket error!", 400)


    ;


    private final int code;
    private final String description;
    private final int httpStatusCode;

    ErrorType(int code, String description, int httpStatusCode) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
