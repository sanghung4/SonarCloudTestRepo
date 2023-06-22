package com.reece.punchoutcustomerbff.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a list of procurement systems.
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProcurementSystemListDto {

  private List<ProcurementSystemDto> procurementSystems = new ArrayList<>();

}
