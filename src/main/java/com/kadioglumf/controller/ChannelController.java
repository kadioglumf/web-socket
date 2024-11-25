package com.kadioglumf.controller;

import com.kadioglumf.dto.request.AddChannelRequestDto;
import com.kadioglumf.dto.request.DeleteChannelRequestDto;
import com.kadioglumf.dto.request.UpdateChannelRequestDto;
import com.kadioglumf.dto.response.ChannelResponseDto;
import com.kadioglumf.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
@Validated
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/refresh")
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

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@Valid @RequestBody UpdateChannelRequestDto request) {
        channelService.updateChannel(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@Valid @RequestBody DeleteChannelRequestDto request) {
        channelService.deleteChannel(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/fetch-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ChannelResponseDto>> fetchAll() {
        return ResponseEntity.ok(channelService.fetchAll());
    }
}
