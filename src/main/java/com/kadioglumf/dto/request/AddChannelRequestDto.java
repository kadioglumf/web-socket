package com.kadioglumf.dto.request;

import com.kadioglumf.dto.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class AddChannelRequestDto implements BaseDto {
    @NotBlank
    private String name;
    private Set<String> roles;
}
