package com.kadioglumf.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificationMarkAsReadRequestDto {
  @NotNull
  @Size(min = 1)
  private List<String> notificationIds;
}
