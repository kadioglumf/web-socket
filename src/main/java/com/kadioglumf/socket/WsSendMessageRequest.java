package com.kadioglumf.socket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsSendMessageRequest {
    @NotNull
    private Object payload;
    @NotNull
    @Convert(converter = WsInfoTypeConverter.class)
    private WsInfoType infoType;
    @NotNull
    @Convert(converter = WsCategoryType.class)
    private WsCategoryType category;
    @NotBlank
    private String channel;

    private WsSendingType sendingType;
    private RoleType role;
    private Long userId;
}
