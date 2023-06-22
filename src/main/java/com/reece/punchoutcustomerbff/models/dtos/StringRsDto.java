package com.reece.punchoutcustomerbff.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * General Response Class.
 */
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class StringRsDto {
  private String message;
}
