package com.kadioglumf.dto.response;

import com.kadioglumf.socket.converter.WsCategoryTypeConverter;
import com.kadioglumf.socket.converter.WsInfoTypeConverter;
import com.kadioglumf.socket.model.enums.WsCategoryType;
import com.kadioglumf.socket.model.enums.WsInfoType;
import java.io.Serializable;
import javax.persistence.Convert;
import lombok.Data;

@Data
public class NotificationResponseDto implements Serializable {
  private String notificationId;
  private String message;

  @Convert(converter = WsInfoTypeConverter.class)
  private WsInfoType infoType;

  @Convert(converter = WsCategoryTypeConverter.class)
  private WsCategoryType category;

  private String channel;
  private Long userId;
  private boolean isRead;
}
