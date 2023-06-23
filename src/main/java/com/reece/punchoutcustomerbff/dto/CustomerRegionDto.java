package com.reece.punchoutcustomerbff.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a region
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerRegionDto {

  private UUID id;

  private String name;

}
