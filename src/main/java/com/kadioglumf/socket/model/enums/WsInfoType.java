package com.kadioglumf.socket.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public enum WsInfoType {

    @JsonProperty("test_info_type") TEST_INFO_TYPE("test_info_type");


    private final String value;

    WsInfoType(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

    public static WsInfoType toAttribute(String value) {
        return Arrays.stream(WsInfoType.values())
                .filter(op -> op.toString().equals(value))
                .findFirst()
                .orElseThrow();
    }
}
