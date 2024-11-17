package com.kadioglumf.config;

import com.kadioglumf.socket.model.ChannelRuleData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChannelConfigData {
    private List<ChannelRuleData> channels;
}

