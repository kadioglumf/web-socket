package com.kadioglumf.socket;

import com.kadioglumf.utils.ConvertUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.TextMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class WebSocketMessages {

  static TextMessage reply(String replyType, @Nullable Set<String> subscribedChannels) {
    Map<String, Object> messageObject = new HashMap<>();
    messageObject.put("type", "reply");
    messageObject.put("replyType", replyType);
    if (subscribedChannels != null) {
      messageObject.put("subscribedChannels", subscribedChannels);
    }
    return new TextMessage(ConvertUtils.toJsonData(messageObject));
  }

  static TextMessage info(WsSendMessageRequest request) {
    Map<String, Object> messageObject = new HashMap<>();
    messageObject.put("type", "info");
    messageObject.put("infoType", request.getInfoType());
    messageObject.put("channel", request.getChannel());
    messageObject.put("payload", request.getPayload());
    messageObject.put("category", request.getCategory());
    return new TextMessage(ConvertUtils.toJsonData(messageObject));
  }

  static TextMessage failure(String failure) {
    Map<String, String> messageObject = new HashMap<>();
    messageObject.put("type", "failure");
    messageObject.put("failureType", failure);
    return new TextMessage(ConvertUtils.toJsonData(messageObject));
  }

  static TextMessage channelMessage(WsSendMessageRequest request) {
    Map<String, Object> messageObject = new HashMap<>();
    messageObject.put("type", "info");
    messageObject.put("infoType", request.getInfoType());
    messageObject.put("channel", request.getChannel());
    messageObject.put("payload", request.getPayload());
    messageObject.put("category", request.getCategory().name());
    return new TextMessage(ConvertUtils.toJsonData(messageObject));
  }
}
