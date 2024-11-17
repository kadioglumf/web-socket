package com.kadioglumf.dto.request;

import com.kadioglumf.dto.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class UpdateChannelRequestDto implements BaseDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    private Set<String> roles;
}
