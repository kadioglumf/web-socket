package com.kadioglumf.service;

import com.kadioglumf.socket.WsSendMessageRequest;

public interface WebSocketService {
    void sendNotification(WsSendMessageRequest request);
}
