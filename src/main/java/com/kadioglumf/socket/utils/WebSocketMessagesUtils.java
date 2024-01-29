package com.kadioglumf.socket.utils;

import com.kadioglumf.socket.model.enums.WsSendMessageRequest;
import com.kadioglumf.utils.ConvertUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.TextMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketMessagesUtils {

  public static TextMessage reply(String replyType, @Nullable Set<String> subscribedChannels) {
    Map<String, Object> messageObject = new HashMap<>();
    messageObject.put("type", "reply");
    messageObject.put("replyType", replyType);
    if (subscribedChannels != null) {
      messageObject.put("subscribedChannels", subscribedChannels);
    }
    return new TextMessage(ConvertUtils.toJsonData(messageObject));
  }

  public static TextMessage info(WsSendMessageRequest request) {
    Map<String, Object> messageObject = new HashMap<>();
    messageObject.put("type", "info");
    messageObject.put("infoType", request.getInfoType());
    messageObject.put("channel", request.getChannel());
    messageObject.put("payload", request.getPayload());
    messageObject.put("category", request.getCategory());
    return new TextMessage(ConvertUtils.toJsonData(messageObject));
  }

  public static TextMessage failure(String failure) {
    Map<String, String> messageObject = new HashMap<>();
    messageObject.put("type", "failure");
    messageObject.put("failureType", failure);
    return new TextMessage(ConvertUtils.toJsonData(messageObject));
  }

  public static TextMessage channelMessage(WsSendMessageRequest request) {
    Map<String, Object> messageObject = new HashMap<>();
    messageObject.put("type", "info");
    messageObject.put("infoType", request.getInfoType());
    messageObject.put("channel", request.getChannel());
    messageObject.put("payload", request.getPayload());
    messageObject.put("category", request.getCategory().name());
    return new TextMessage(ConvertUtils.toJsonData(messageObject));
  }
}
