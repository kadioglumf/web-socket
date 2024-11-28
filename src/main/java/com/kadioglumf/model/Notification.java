package com.kadioglumf.model;

import com.kadioglumf.socket.converter.WsCategoryTypeConverter;
import com.kadioglumf.socket.converter.WsInfoTypeConverter;
import com.kadioglumf.socket.model.enums.WsCategoryType;
import com.kadioglumf.socket.model.enums.WsInfoType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Entity
@Table(
    name = "notification",
    indexes = {@Index(name = "idx_user_id_is_deleted", columnList = "userId, isDeleted")})
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Where(clause = "is_deleted = false")
public class Notification extends AbstractModel {
  private String notificationId;
  private String message;

  @Convert(converter = WsInfoTypeConverter.class)
  private WsInfoType infoType;

  @Convert(converter = WsCategoryTypeConverter.class)
  private WsCategoryType category;

  private String channel;
  private Long userId;
  private boolean isRead;
  private boolean isDeleted;
}
