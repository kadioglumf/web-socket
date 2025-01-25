package com.kadioglumf.socket.model.enums;

public enum ActionType {
  SUBSCRIBE("subscribe"),
  UNSUBSCRIBE("unsubscribe"),
  SEND_MESSAGE("send"),
  REFRESH_CONNECTION("refresh_connection"),
  SUBSCRIBE_ALL("subscribe_all");

  private final String value;

  ActionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static ActionType getFromValue(String value) {
    for (ActionType type : values()) {
      if (type.getValue().equals(value)) {
        return type;
      }
    }
    return null;
  }
}
