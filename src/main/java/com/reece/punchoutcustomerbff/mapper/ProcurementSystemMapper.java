package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.ProcurementSystemDto;
import com.reece.punchoutcustomerbff.models.daos.ProcurementSystemDao;
import java.util.ArrayList;
import java.util.List;

/**
 * Used for mapping between ProcurementSystem entities and DTOs.
 * @author john.valentino
 */
public class ProcurementSystemMapper {

  public static List<ProcurementSystemDto> toDTOs(List<ProcurementSystemDao> inputs) {
    List<ProcurementSystemDto> outputs = new ArrayList<>();
    for (ProcurementSystemDao input : inputs) {
      outputs.add(ProcurementSystemMapper.toDTO(input));
    }
    return outputs;
  }

  public static ProcurementSystemDto toDTO(ProcurementSystemDao input) {
    ProcurementSystemDto output = ProcurementSystemDto.builder()
        .name(input.getName())
        .build();
    return output;
  }

}
