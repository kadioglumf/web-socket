package com.kadioglumf.dto.request;

import com.kadioglumf.dto.BaseDto;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddChannelRequestDto implements BaseDto {
  @NotBlank private String name;
  private Set<String> roles;
}
