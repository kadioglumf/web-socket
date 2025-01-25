package com.kadioglumf.dto.response;

import com.kadioglumf.dto.BaseDto;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelResponseDto implements BaseDto {
  private String name;
  private Set<String> roles;
  private List<UserPreferencesResponseDto> userPreferences;
}
