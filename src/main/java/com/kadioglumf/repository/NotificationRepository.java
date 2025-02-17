package com.kadioglumf.repository;

import com.kadioglumf.model.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findByUserId(Long userId);

  List<Notification> findByNotificationIdIn(List<String> notificationIds);
}
