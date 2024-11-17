package com.kadioglumf.dto.response;

import com.kadioglumf.dto.BaseDto;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ChannelResponseDto implements BaseDto {
    private Long id;
    private String name;
    private Set<String> roles;
}
