package com.kadioglumf.socket.model.enums;

import com.kadioglumf.socket.converter.WsInfoTypeConverter;
import javax.persistence.Convert;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WsSendMessageRequest {
  @NotNull private Object payload;

  @NotNull
  @Convert(converter = WsInfoTypeConverter.class)
  private WsInfoType infoType;

  @NotNull
  @Convert(converter = WsCategoryType.class)
  private WsCategoryType category;

  @NotBlank private String channel;

  private WsSendingType sendingType;
  private RoleType role;
  private Long userId;
}
