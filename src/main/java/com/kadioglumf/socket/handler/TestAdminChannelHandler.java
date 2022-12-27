package com.kadioglumf.socket.handler;


import com.kadioglumf.socket.annotations.ChannelHandler;

@ChannelHandler(value = "test-admin-channel", allowedRoles = {"ROLE_ADMIN", "ROLE_MODERATOR"})
public class TestAdminChannelHandler extends BaseChannelHandler {

}
