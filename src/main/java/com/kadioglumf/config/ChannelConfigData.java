package com.kadioglumf.config;

import com.kadioglumf.socket.model.ChannelRuleData;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelConfigData {
  private List<ChannelRuleData> channels;
}
