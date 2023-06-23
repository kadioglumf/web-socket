package com.kadioglumf.service.impl;

import com.kadioglumf.service.WebSocketService;
import com.kadioglumf.socket.SubscriptionHub;
import com.kadioglumf.socket.WsSendMessageRequest;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Override
    public void sendNotification(WsSendMessageRequest request) {
        SubscriptionHub.send(request);
    }
}
