package com.kadioglumf.socket.validator;

import com.kadioglumf.config.ChannelConfigData;
import com.kadioglumf.dto.response.ChannelResponseDto;
import com.kadioglumf.exception.ErrorType;
import com.kadioglumf.exception.WebSocketException;
import com.kadioglumf.service.ChannelService;
import com.kadioglumf.socket.model.ChannelRuleData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ChannelValidator {
    private final ChannelService channelService;

    public void valid(String channel, List<String> userRoles) {
        ChannelResponseDto channelInfo = channelService.fetchAll()
                .stream()
                .filter(info -> info.getName().equals(channel))
                .findFirst()
                .orElseThrow(() -> new WebSocketException(ErrorType.WEB_SOCKET_ERROR, "Channel `" + channel + "` not found!"));

        if (!isRolesAllowed(userRoles, channelInfo.getRoles())) {
            throw new WebSocketException(ErrorType.WEB_SOCKET_ERROR, "You are not allowed to subscribe this channel!");
        }
    }

    public boolean isRolesAllowed(List<String> roles, Set<String> allowedRoles) {
        if (CollectionUtils.isEmpty(allowedRoles)) {
            return true;
        }
        for (String role : roles) {
            for (String allowedRole : allowedRoles) {
                if (role.equals(allowedRole)) {
                    return true;
                }
            }
        }
        return false;
    }
}
