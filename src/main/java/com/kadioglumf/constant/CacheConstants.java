package com.kadioglumf.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheConstants {
  public static final String CHANNEL_CACHE_VALUE = "websocket-channels";
  public static final String CHANNEL_CACHE_KEY = "channel-list";

  public static final String USER_CHANNEL_CACHE_VALUE = "websocket-user-channels";
  public static final String USER_CHANNEL_CACHE_KEY = "user-channel";

  public static final String NOTIFICATION_CACHE_VALUE = "websocket-notifications";
}
