package com.kadioglumf.service;

import com.kadioglumf.socket.model.enums.WsSendMessageRequest;
import com.kadioglumf.socket.utils.SubscriptionHubUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WebSocketService {
  private final NotificationService notificationService;

  public void sendNotification(WsSendMessageRequest request) {
    SubscriptionHubUtils.send(request);
    notificationService.save(request);
  }
}
