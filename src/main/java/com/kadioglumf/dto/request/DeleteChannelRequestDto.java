package com.kadioglumf.dto.request;

import com.kadioglumf.dto.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeleteChannelRequestDto implements BaseDto {
    @NotBlank
    private String name;
}
