package com.kadioglumf.socket.handler;


import com.kadioglumf.socket.annotations.ChannelHandler;

@ChannelHandler(value = "test-channel", allowedRoles = {"ROLE_ADMIN", "ROLE_MODERATOR"})
public class TestChannelHandler extends BaseChannelHandler {

}
