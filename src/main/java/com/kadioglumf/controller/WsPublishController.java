package com.kadioglumf.controller;

import com.kadioglumf.socket.SubscriptionHub;
import com.kadioglumf.socket.WsSendMessageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web-socket/publish")
public class WsPublishController {

    @PostMapping
    public void publish(@RequestBody WsSendMessageRequest request) {
        SubscriptionHub.send(request);
    }
}
