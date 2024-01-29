package com.kadioglumf.service;

import com.kadioglumf.socket.model.enums.WsSendMessageRequest;

public interface WebSocketService {
    void sendNotification(WsSendMessageRequest request);
}
