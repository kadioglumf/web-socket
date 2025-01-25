package com.kadioglumf.repository;

import com.kadioglumf.model.UserChannelPreferences;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChannelPreferencesRepository
    extends JpaRepository<UserChannelPreferences, Long> {
  List<UserChannelPreferences> findAllByUserId(Long userId);

  List<UserChannelPreferences> findAllByChannel_Name(String channelName);
}
