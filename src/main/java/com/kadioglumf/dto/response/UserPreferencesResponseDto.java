package com.kadioglumf.dto.response;

import com.kadioglumf.dto.BaseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPreferencesResponseDto implements BaseDto {
  private Long userId;
  private boolean isSubscribed;
  private String channel;
}
