package com.kadioglumf.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.*;

@Entity
@Table(name = "user_channel_preferences")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChannelPreferences extends AbstractModel {
  private Long userId;
  private boolean isSubscribed;

  @ManyToOne private Channel channel;
}
