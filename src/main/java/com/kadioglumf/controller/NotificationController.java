package com.kadioglumf.controller;

import com.kadioglumf.dto.request.NotificationMarkAsReadRequestDto;
import com.kadioglumf.dto.response.NotificationResponseDto;
import com.kadioglumf.service.NotificationService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
  private final NotificationService notificationService;

  @GetMapping(value = "/getAll")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<List<NotificationResponseDto>> fetch() {
    return ResponseEntity.ok(notificationService.fetch());
  }

  @DeleteMapping(value = "/deleteAll")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<Void> deleteAll() {
    notificationService.deleteAll();
    return ResponseEntity.ok().build();
  }

  @PutMapping(value = "/markAsRead")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<Void> markAsRead(
      @RequestBody @Valid NotificationMarkAsReadRequestDto request) {
    notificationService.markAsRead(request.getNotificationIds());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/cache/refresh")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> refresh() {
    notificationService.refreshNotifications();
    return ResponseEntity.ok().build();
  }
}
