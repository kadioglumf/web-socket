package com.kadioglumf.dto.request;

import com.kadioglumf.dto.BaseDto;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateChannelRequestDto implements BaseDto {
  @NotNull private Long id;
  @NotBlank private String name;
  private Set<String> roles;
}
