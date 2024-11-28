package com.kadioglumf.controller;

import com.kadioglumf.dto.request.*;
import com.kadioglumf.dto.response.ChannelResponseDto;
import com.kadioglumf.dto.response.UserPreferencesResponseDto;
import com.kadioglumf.service.ChannelService;
import com.kadioglumf.service.UserChannelPreferencesService;
import com.kadioglumf.utils.UserThreadContext;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
@Validated
public class ChannelController {
  private final ChannelService channelService;
  private final UserChannelPreferencesService userChannelPreferencesService;

  @PostMapping("/cache/refresh")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> refresh() {
    channelService.refreshChannels();
    return ResponseEntity.ok().build();
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> add(@Valid @RequestBody AddChannelRequestDto request) {
    channelService.addChannel(request);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/delete")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@Valid @RequestBody DeleteChannelRequestDto request) {
    channelService.deleteChannel(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/getAll")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<ChannelResponseDto>> getAll() {
    return ResponseEntity.ok(channelService.getAll());
  }

  @PostMapping("/subscribe")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<Void> subscribe(@Valid @RequestBody SubscribeChannelRequestDto request) {
    channelService.subscribeChannel(request.getName(), UserThreadContext.getUserId());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/unsubscribe")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<Void> unsubscribe(
      @Valid @RequestBody UnsubscribeChannelRequestDto request) {
    channelService.unsubscribeChannel(request.getName(), UserThreadContext.getUserId());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/preferences/getUserChannels")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<List<UserPreferencesResponseDto>> getUserChannels() {
    return ResponseEntity.ok(
        userChannelPreferencesService.getUserChannels(UserThreadContext.getUserId()));
  }
}
