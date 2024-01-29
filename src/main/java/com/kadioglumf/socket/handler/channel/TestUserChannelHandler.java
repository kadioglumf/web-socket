package com.kadioglumf.socket.handler.channel;

import com.kadioglumf.socket.annotations.ChannelHandler;

@ChannelHandler(value = "test-user-channel", allowedRoles = {"ROLE_USER"})
public class TestUserChannelHandler extends BaseChannelHandler {

}
