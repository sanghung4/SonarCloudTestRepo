package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.CustomerRegionDto;
import com.reece.punchoutcustomerbff.models.daos.CustomerRegionDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Persistence;

/**
 * Used for mapping to and from CustomerRegion entities and DTOs
 * @author john.valentino
 */
public class CustomerRegionMapper {

  public static List<CustomerRegionDto> toDTOs(Set<CustomerRegionDao> inputs) {
    if (inputs == null || !Persistence.getPersistenceUtil().isLoaded(inputs)) {
      return new ArrayList<>();
    }
    List<CustomerRegionDto> outputs = new ArrayList<>();
    for (CustomerRegionDao input : inputs) {
      outputs.add(CustomerRegionMapper.toDTO(input));
    }
    return outputs;
  }

  public static CustomerRegionDto toDTO(CustomerRegionDao input) {
    CustomerRegionDto output = CustomerRegionDto.builder()
        .id(input.getId())
        .name(input.getName())
        .build();
    return output;
  }

}
