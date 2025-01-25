package com.kadioglumf.mapper;

import com.kadioglumf.dto.response.NotificationResponseDto;
import com.kadioglumf.model.Notification;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  List<NotificationResponseDto> toNotificationResponseList(List<Notification> models);
}
