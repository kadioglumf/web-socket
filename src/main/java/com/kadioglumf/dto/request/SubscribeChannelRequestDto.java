package com.kadioglumf.dto.request;

import com.kadioglumf.dto.BaseDto;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubscribeChannelRequestDto implements BaseDto {
  @NotBlank private String name;
}
