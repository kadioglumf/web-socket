package com.kadioglumf.controller;

import com.kadioglumf.service.WebSocketService;
import com.kadioglumf.socket.SubscriptionHub;
import com.kadioglumf.socket.WsSendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web-socket/publish")
@Controller
@RequiredArgsConstructor
public class WsPublishController {

    private final WebSocketService webSocketService;
    @PostMapping
    public void publish(@RequestBody WsSendMessageRequest request) {
        webSocketService.sendNotification(request);
    }
}
