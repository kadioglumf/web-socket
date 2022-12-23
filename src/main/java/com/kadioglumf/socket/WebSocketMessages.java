package com.kadioglumf.socket;

import com.kadioglumf.utils.ConvertUtils;
import org.springframework.web.socket.TextMessage;

import java.util.HashMap;
import java.util.Map;

class WebSocketMessages {

  static TextMessage reply(String reply) {
    Map<String, String> messageObject = new HashMap<>();
    messageObject.put("type", "reply");
    messageObject.put("message", reply);
    return new TextMessage(ConvertUtils.ToJsonData(messageObject));
  }

  static TextMessage error(String error) {
    Map<String, String> messageObject = new HashMap<>();
    messageObject.put("type", "error");
    messageObject.put("message", error);
    return new TextMessage(ConvertUtils.ToJsonData(messageObject));
  }

  static TextMessage failure(String failure) {
    Map<String, String> messageObject = new HashMap<>();
    messageObject.put("type", "failure");
    messageObject.put("message", failure);
    return new TextMessage(ConvertUtils.ToJsonData(messageObject));
  }

  static TextMessage channelMessage(WsSendMessageRequest request) {
    Map<String, Object> messageObject = new HashMap<>();
    messageObject.put("channel", request.getChannel());
    messageObject.put("payload", request.getPayload());
    messageObject.put("event", request.getEvent().name());
    messageObject.put("category", request.getCategory().name());
    return new TextMessage(ConvertUtils.ToJsonData(messageObject));
  }
}
