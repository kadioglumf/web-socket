package com.kadioglumf.service.impl;

import com.kadioglumf.service.WebSocketService;
import com.kadioglumf.socket.utils.SubscriptionHubUtils;
import com.kadioglumf.socket.model.enums.WsSendMessageRequest;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Override
    public void sendNotification(WsSendMessageRequest request) {
        SubscriptionHubUtils.send(request);
    }
}
