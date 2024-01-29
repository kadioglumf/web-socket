package com.kadioglumf.socket.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public enum WsCategoryType {

    @JsonProperty("test_category_type") TEST_CATEGORY_TYPE("test_category_type"),
    ;

    private final String value;

    WsCategoryType(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

    public static WsCategoryType toAttribute(String value) {
        return Arrays.stream(WsCategoryType.values())
                .filter(op -> op.toString().equals(value))
                .findFirst()
                .orElseThrow();
    }
}
