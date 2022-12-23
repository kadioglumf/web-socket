package com.kadioglumf.socket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsSendMessageRequest {
    private String channel;
    private Object payload;
    private WsCategoryType category;
    private WsEventType event;
}
