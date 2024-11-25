package com.kadioglumf.service;

import com.kadioglumf.socket.utils.SubscriptionHubUtils;
import com.kadioglumf.socket.model.enums.WsSendMessageRequest;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    public void sendNotification(WsSendMessageRequest request) {
        SubscriptionHubUtils.send(request);
    }
}
